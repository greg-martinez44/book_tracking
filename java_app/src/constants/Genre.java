package base.constants;

public enum Genre {
    SCIFI {
        @Override
        public String toString() {
            return "Sci-Fi";
        }
    },
    NATURE,
    FANTASY,
    GENERAL_FICTION,
    COMEDY,
    HISTORY,
    PSYCHOLOGY,
    MEMOIR,
    GENERAL_NONFICTION,
    POLITICS,
    SELF_HELP,
    PHILOSOPHY,
    TECHNOLOGY,
    FOOD,
    COMP_SCI,
    ART,
    POEMS,
    ROMANCE,
    GAMING,
    HORROR,
    MYSTERY,
    YOUNG_ADULT,
    JOURNAL
}
