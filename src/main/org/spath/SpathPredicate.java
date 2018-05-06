package org.spath;


public interface SpathPredicate extends SpathMatch {
    
    public String getName();
//    {
//        return name;
//    }

    public SpathOperator getOperator();
//    {
//        return operator;
//    }

    public String getValue();
//    {
//        return value;
//    }
    
//    @Override
//    public <T> boolean match(SpathEvaluator<T> eval, T event) {
//        return eval.match(this, event);
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        } else if (other instanceof SpathPredicate) {
//            return toString().equals(other.toString());
//        } else {
//            return false;
//        }
//    }
//    
//    @Override
//    public String toString() {
//        StringBuilder build = new StringBuilder();
//        build.append("@");
//        build.append(name);
//        if (operator != null) {
//            build.append(operator);
//            build.append('\'');
//            build.append(value);
//            build.append('\'');
//        }
//        return build.toString();
//    }
}
