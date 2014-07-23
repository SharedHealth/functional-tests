package domain;

public class ConceptReferenceTerm {

    private String code;
    private String name;
    private String source;
    private String description;
    private String version;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }



    private ConceptReferenceTerm(ConceptReferenceTermBuilder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.source = builder.source;
        this.description = builder.description;
        this.version = builder.version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", source='").append(source).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class ConceptReferenceTermBuilder {

        private String code;
        private String name;
        private String source;
        private String description;
        private String version;

        public ConceptReferenceTermBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ConceptReferenceTermBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ConceptReferenceTermBuilder source(String source) {
            this.source = source;
            return this;
        }

        public ConceptReferenceTermBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ConceptReferenceTermBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ConceptReferenceTerm build() {
            return new ConceptReferenceTerm(this);
        }
    }
}
