package ast;

/**
 * A factory that produces the public static Mutation objects corresponding to each
 * mutation
 */
public class MutationFactory {
    public static Mutation getRemove() {
        return new MutationRemove();
    }

    public static Mutation getSwap() {
        return new MutationSwap();
    }

    public static Mutation getReplace() {
        return new MutationReplace();
    }

    public static Mutation getTransform() {
        return new MutationTransform();
    }

    public static Mutation getInsert() {
        return new MutationInsert();
    }

    public static Mutation getDuplicate() {
        return new MutationDuplicate();
    }
}
