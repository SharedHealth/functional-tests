package data;

import domain.Concept;
import domain.ConceptReferenceTerm;

public class ConceptData {

    private String id = String.valueOf(System.currentTimeMillis()).substring(7);
    public ConceptReferenceTerm conceptReferenceTerm = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + id)
            .name("Viral pneumonia " + id)
            .source("ICD10-BD [ICD10-BD-A]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id)
            .version("1.0")
            .build();

    public ConceptReferenceTerm conceptReferenceTermForEdit = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + id)
            .name("Viral pneumonia " + id+" Changed")
            .source("ICD10-BD [ICD10-BD-A]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.: Changed \n" +
                    id)
            .version("1.1")
            .build();


    public Concept conceptForDiagnosis = new Concept.ConceptBuilder()
            .name("Viral pneumonia " + id)
            .synonyms1("Viral pneumonia " + id + " Syn")
            .synonyms2("Viral pneumonia " + id + " Syn2")
            .shortName("ViralPneumonia" + id)
            .description("Viral pneumonia, unspecified \n "
                    + id)
            .conceptClass("Diagnosis")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTerm.getCode())
            .conceptMappingName(conceptReferenceTerm.getName())
            .build();

    public Concept conceptForDiagnosisEdit = new Concept.ConceptBuilder()
            .name("Viral pneumonia " + id)
            .synonyms1("Viral pneumonia " + id + " Syn Changed")
            .synonyms2("Viral pneumonia " + id + " Syn2")
            .shortName("ViralPneumonia" + id + "Changed")
            .description("Viral pneumonia, unspecified \n "
                    + id)
            .conceptClass("Diagnosis")
            .dataType("N/A")
            .version("1.1")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTerm.getCode())
            .conceptMappingName(conceptReferenceTerm.getName())
            .build();

    public String idForVerification = "000839";
    public ConceptReferenceTerm conceptReferenceTermForVerification = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + idForVerification)
            .name("Viral pneumonia " + idForVerification)
            .source("ICD10-BD [ICD10-BD]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    idForVerification)
            .version("1.0")
            .build();

    public Concept conceptForDiagnosisForVerification = new Concept.ConceptBuilder()
            .name("Viral pneumonia " + idForVerification)
            .synonyms1("Viral pneumonia " + idForVerification + " Syn")
            .synonyms2("Viral pneumonia " + idForVerification + " Syn2")
            .shortName("ViralPneumonia" + idForVerification)
            .description("Viral pneumonia, unspecified \n "
                    + idForVerification)
            .conceptClass("Diagnosis")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTermForVerification.getCode())
            .conceptMappingName(conceptReferenceTermForVerification.getName())
            .build();

    private String id2 = "207984";
    public ConceptReferenceTerm getConceptReferenceTermForVerification = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + id2)
            .name("Viral pneumonia " + id2)
            .source("ICD10-BD [ICD10-BD-A]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id2)
            .version("1.0")
            .build();

}
