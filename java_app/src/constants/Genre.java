public enum Genre {
    SCIFI {
        @Override
        public String toString() {
            return "Sci-Fi";
        }
    },
    NATURE,
    FANTASY,
    GENERIC
}