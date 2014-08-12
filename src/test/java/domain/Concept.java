package domain;

public class Concept {

    private String name;
    private String synonyms1;
    private String synonyms2;
    private String shortName;
    private String description;
    private String conceptClass;
    private String dataType;
    private String version;
    private String conceptMappingRelationship;
    private String conceptMappingSource;
    private String conceptMappingCode;
    private String conceptMappingName;
    private String numericAbsoluteHigh;
    private String numericCriticalHigh;
    private String numericNormalHigh;
    private String numericAbsoluteLow;
    private String numericCriticalLow;
    private String numericNormalLow;
    private String numericUnit;


    public String getSynonyms1() {
        return synonyms1;
    }

    public String getSynonyms2() {
        return synonyms2;
    }

    public String getShortName() {
        return shortName;
    }

    public String getConceptClass() {
        return conceptClass;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getDataType() {
        return dataType;
    }

    public String getConceptMappingName() {
        return conceptMappingName;
    }

    public String getConceptMappingRelationship() {
        return conceptMappingRelationship;
    }

    public String getConceptMappingSource() {
        return conceptMappingSource;
    }

    public String getConceptMappingCode() {
        return conceptMappingCode;
    }

    public String getNumericAbsoluteHigh() {
        return numericAbsoluteHigh;
    }

    public String getNumericCriticalHigh() {
        return numericCriticalHigh;
    }

    public String getNumericNormalHigh() {
        return numericNormalHigh;
    }

    public String getNumericAbsoluteLow() {
        return numericAbsoluteLow;
    }

    public String getNumericCriticalLow() {
        return numericCriticalLow;
    }

    public String getNumericNormalLow() {
        return numericNormalLow;
    }


    private Concept(ConceptBuilder builder) {

        this.name = builder.name;
        this.synonyms1 = builder.synonyms1;
        this.synonyms2 = builder.synonyms2;
        this.shortName = builder.shortName;
        this.description = builder.description;
        this.conceptClass = builder.conceptClass;
        this.dataType = builder.dataType;
        this.version = builder.version;
        this.conceptMappingRelationship = builder.conceptMappingRelationship;
        this.conceptMappingSource = builder.conceptMappingSource;
        this.conceptMappingCode = builder.conceptMappingCode;
        this.conceptMappingName = builder.conceptMappingName;
        this.numericAbsoluteHigh = builder.numericAbsoluteHigh;
        this.numericCriticalHigh = builder.numericCriticalHigh;
        this.numericNormalHigh = builder.numericNormalHigh;
        this.numericAbsoluteLow = builder.numericAbsoluteLow;
        this.numericCriticalLow = builder.numericCriticalLow;
        this.numericNormalLow = builder.numericNormalLow;


    }

    public String getNumericUnit() {
        return numericUnit;
    }

    public static class ConceptBuilder {

        private String name;
        private String synonyms1;
        private String synonyms2;
        private String shortName;
        private String description;
        private String conceptClass;
        private String dataType;
        private String version;
        private String conceptMappingRelationship;
        private String conceptMappingSource;
        private String conceptMappingCode;
        private String conceptMappingName;
        private String numericAbsoluteHigh;
        private String numericCriticalHigh;
        private String numericNormalHigh;
        private String numericAbsoluteLow;
        private String numericCriticalLow;
        private String numericNormalLow;
        private String numericUnit;


        public ConceptBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ConceptBuilder synonyms1(String synonyms1) {
            this.synonyms1 = synonyms1;
            return this;
        }

        public ConceptBuilder synonyms2(String synonyms2) {
            this.synonyms2 = synonyms2;
            return this;
        }

        public ConceptBuilder shortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public ConceptBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ConceptBuilder conceptClass(String conceptClass) {
            this.conceptClass = conceptClass;
            return this;
        }

        public ConceptBuilder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public ConceptBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ConceptBuilder conceptMappingRelationship(String conceptMappingRelationship) {
            this.conceptMappingRelationship = conceptMappingRelationship;
            return this;
        }

        public ConceptBuilder conceptMappingSource(String conceptMappingSource) {
            this.conceptMappingSource = conceptMappingSource;
            return this;
        }

        public ConceptBuilder conceptMappingCode(String conceptMappingCode) {
            this.conceptMappingCode = conceptMappingCode;
            return this;
        }

        public ConceptBuilder conceptMappingName(String conceptMappingName) {
            this.conceptMappingName = conceptMappingName;
            return this;
        }

        public ConceptBuilder numericCriticalHigh(String numericCriticalHigh) {
            this.numericCriticalHigh = numericCriticalHigh;
            return this;
        }

        public ConceptBuilder numericAbsoluteHigh(String numericAbsoluteHigh) {
            this.numericAbsoluteHigh = numericAbsoluteHigh;
            return this;
        }

        public ConceptBuilder numericNormalHigh(String numericNormalHigh) {
            this.numericNormalHigh = numericNormalHigh;
            return this;
        }

        public ConceptBuilder numericAbsoluteLow(String numericAbsoluteLow) {
            this.numericAbsoluteLow = numericAbsoluteLow;
            return this;
        }

        public ConceptBuilder numericCriticalLow(String numericCriticalLow) {
            this.numericCriticalLow = numericCriticalLow;
            return this;
        }

        public ConceptBuilder numericNormalLow(String numericNormalLow) {
            this.numericNormalLow = numericNormalLow;
            return this;
        }

        public ConceptBuilder numericUnit(String numericUnit) {
            this.numericUnit = numericUnit;
            return this;
        }

        public Concept build() {
            return new Concept(this);
        }
    }
}
