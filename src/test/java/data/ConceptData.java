package data;

import domain.Concept;
import domain.ConceptReferenceTerm;

public class ConceptData {

    private static String id = String.valueOf(System.currentTimeMillis()).substring(7);
    public static ConceptReferenceTerm conceptReferenceTerm = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19."+id)
            .name("Viral pneumonia "+id)
            .source("ICD10-BD [ICD10-BD]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id)
            .version("1.0")
            .build();

    public static Concept conceptForDiagnosis = new Concept.ConceptBuilder()
     .name("Viral pneumonia " + id)
     .synonyms1("Viral pneumonia "+id+" Syn")
     .synonyms2("Viral pneumonia "+id+" Syn2")
     .shortName("ViralPneumonia"+id)
     .description("Viral pneumonia, unspecified \n "
                   +id)
     .conceptClass("Diagnosis")
     .dataType("N/A")
     .version("1.0")
     .conceptMappingRelationship("SAME-AS")
     .conceptMappingSource("ICD10-BD")
     .conceptMappingCode(conceptReferenceTerm.getCode())
     .conceptMappingName(conceptReferenceTerm.getName())
      .build();

    public static String id1 =  "055673";
    public static ConceptReferenceTerm conceptReferenceTermForVerification = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19."+id1)
            .name("Viral pneumonia "+id1)
            .source("ICD10-BD [ICD10-BD]")
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id1)
            .version("1.0")
            .build();

    public static Concept conceptForDiagnosisForVerification = new Concept.ConceptBuilder()
     .name("Viral pneumonia " + id1)
     .synonyms1("Viral pneumonia "+id1+" Syn")
     .synonyms2("Viral pneumonia "+id1+" Syn2")
     .shortName("ViralPneumonia"+id1)
     .description("Viral pneumonia, unspecified \n "
                   +id1)
     .conceptClass("Diagnosis")
     .dataType("N/A")
     .version("1.0")
     .conceptMappingRelationship("SAME-AS")
     .conceptMappingSource("ICD10-BD")
     .conceptMappingCode(conceptReferenceTermForVerification.getCode())
     .conceptMappingName(conceptReferenceTermForVerification.getName())
      .build();


}
