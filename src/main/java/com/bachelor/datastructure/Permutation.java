package com.bachelor.datastructure;

public class Permutation {
    public int matchStart;
    public int matchEnd;

    private Permutation(Builder builder){
        this.matchStart = builder.matchStart;
        this.matchEnd = builder.matchEnd;
    }

    public static class Builder{
        private int matchStart;
        private int matchEnd;

        public Builder matchStart(int matchStart){
            this.matchStart = matchStart;
            return this;
        }

        public Builder matchEnd(int matchEnd){
            this.matchEnd = matchEnd;
            return this;
        }

        public Permutation build(){
            return new Permutation(this);
        }
    }

    @Override
    public String toString() {
        return "Permutation{" +
                "matchStart=" + matchStart +
                ", matchEnd=" + matchEnd +
                '}';
    }
}
