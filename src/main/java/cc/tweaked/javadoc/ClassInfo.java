/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package cc.tweaked.javadoc;

import com.sun.source.doctree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Information about a class.
 */
public class ClassInfo {
    public static final String PERIPHERAL = "peripheral";
    public static final String GENERIC_PERIPHERAL = "generic_peripheral";
    public static final String API = "module";

    private final String name;
    private final String kind;
    private final Sort sort;
    private final TypeElement element;
    private final DocCommentTree doc;
    private final boolean hidden;
    private final String moduleName;
    private final String typeName;
    private final String referenceName;
    private final boolean attaches;
    @Nullable private final Set<String> attachTargets;

    private ClassInfo(@Nonnull String module, @Nonnull String kind, @Nonnull Sort sort, @Nonnull TypeElement element, @Nonnull DocCommentTree doc, boolean hidden, boolean attaches, @Nullable Set<String> attachTargets) {
        this.name = module;
        this.kind = kind;
        this.sort = sort;
        this.element = element;
        this.doc = doc;
        this.hidden = hidden;
        this.attaches = attaches;
        this.attachTargets = attachTargets;

        if (sort != Sort.TYPE) {
            moduleName = name;
            typeName = null;
        } else {
            int index = name.indexOf('.');
            moduleName = index < 0 ? name : name.substring(0, index);
            typeName = index < 0 ? name : name.substring(index + 1);
        }

        referenceName = kind.isEmpty() ? name : kind + "!" + name;
    }

    /**
     * Attempt to construct a {@link ClassInfo}.
     *
     * @param env  The environment to construct within.
     * @param type The method we're wrapping.
     * @return Information about this method, if available.
     */
    @Nonnull
    public static Optional<ClassInfo> of(@Nonnull Environment env, @Nonnull TypeElement type) {
        DocCommentTree doc = env.trees().getDocCommentTree(type);
        if (doc == null) return Optional.empty();

        String name = doc.getBlockTags().stream()
            .filter(UnknownBlockTagTree.class::isInstance).map(UnknownBlockTagTree.class::cast)
            .filter(x -> x.getTagName().equals("cc.module"))
            .map(UnknownBlockTagTree::getContent)
            .findAny().map(ClassInfo::getName).orElse(null);

        boolean hidden = doc.getBlockTags().stream().anyMatch(x -> x.getKind() == DocTree.Kind.HIDDEN);
        Optional<UnknownBlockTagTree> attachTag = doc.getBlockTags().stream()
            .filter(UnknownBlockTagTree.class::isInstance).map(UnknownBlockTagTree.class::cast)
            .filter(x -> x.getTagName().equals("cc.attach"))
            .findFirst();
        boolean attaches = attachTag.isPresent();
        Set<String> attachTargets = null;
        if (attaches) {
            String content = getName(attachTag.get().getContent()).trim();
            if (!content.isEmpty()) {
                attachTargets = Collections.unmodifiableSet(
                    new LinkedHashSet<>(Arrays.asList(content.split("\\s+"))));
            }
        }

        if (name == null || name.isEmpty()) return Optional.empty();

        Sort sort;
        String kind;
        if (env.types().isAssignable(type.asType(), env.getLuaApiType())) {
            sort = Sort.MODULE;
            kind = API;
        } else if (env.types().isAssignable(type.asType(), env.getPeripheralType())) {
            sort = Sort.MODULE;
            kind = PERIPHERAL;
        } else if (env.types().isAssignable(type.asType(), env.getGenericPeripheralType())) {
            sort = Sort.MODULE;
            kind = GENERIC_PERIPHERAL;
        } else {
            sort = Sort.TYPE;

            if (name.startsWith("[kind=")) {
                int end = name.indexOf(']');
                if (end < 0) {
                    env.message(Diagnostic.Kind.ERROR, "Invalid module name " + name + " in doc comment", type);
                    return Optional.empty();
                }

                kind = name.substring("[kind=".length(), end);
                name = name.substring(end + 1).stripLeading();
            } else {
                kind = "";
            }
        }

        return Optional.of(new ClassInfo(name, kind, sort, type, doc, hidden, attaches, attachTargets));
    }

    private static String getName(List<? extends DocTree> tree) {
        if (tree == null) return null;
        StringBuilder builder = new StringBuilder();
        for (DocTree child : tree) {
            switch (child.getKind()) {
                case TEXT -> builder.append(((TextTree) child).getBody());
                case MARKDOWN -> builder.append(((RawTextTree) child).getContent());
                default -> {
                }
            }
        }
        return builder.toString();
    }

    public enum Sort {
        MODULE,
        TYPE,
    }

    @Nonnull
    public String moduleName() {
        return moduleName;
    }

    @Nullable
    public String typeName() {
        return typeName;
    }

    @Nonnull
    public String name() {
        return name;
    }

    @Nonnull
    public String referenceName() {
        return referenceName;
    }

    @Nonnull
    public String kind() {
        return kind;
    }

    @Nonnull
    public Sort sort() {
        return sort;
    }

    @Nonnull
    public TypeElement element() {
        return element;
    }

    @Nonnull
    public DocCommentTree doc() {
        return doc;
    }

    public boolean isHidden() {
        return hidden;
    }

    /**
     * Whether this generic-source class opts into auto-attachment via a
     * {@code @cc.attach} block tag. When {@link #attachTargets()} is non-null
     * the tag listed explicit module names; otherwise attachment is inferred
     * from the source's receiver type.
     */
    public boolean attaches() {
        return attaches;
    }

    /**
     * Explicit list of target module names from {@code @cc.attach}, or
     * {@code null} when the tag carried no value (use receiver-type inference).
     */
    @Nullable
    public Set<String> attachTargets() {
        return attachTargets;
    }
}
