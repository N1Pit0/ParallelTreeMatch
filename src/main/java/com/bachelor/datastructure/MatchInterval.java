package com.bachelor.datastructure;

public class MatchInterval {
    public int matchStart;
    public int matchEnd;

     private MatchInterval(Builder builder){
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

        public MatchInterval build(){
            return new MatchInterval(this);
        }
    }

    @Override
    public String toString() {
        return "MatchInterval{" +
                "matchStart=" + matchStart +
                ", matchEnd=" + matchEnd +
                '}';
    }
}
