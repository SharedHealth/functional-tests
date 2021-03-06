package data;

import domain.Concept;
import domain.ConceptReferenceTerm;

public class ConceptData {

    public static final String CONCEPT_SOURCE = "ICD10-BD [ICD10-BD]";
//    public static final String CONCEPT_SOURCE = "ICD10-BD [ICD10-BD-A]";
    private String id = String.valueOf(System.currentTimeMillis()).substring(7);
    public ConceptReferenceTerm conceptReferenceTerm = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + id)
            .name("Viral pneumonia " + id)
            .source(CONCEPT_SOURCE)
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id)
            .version("1.0")
            .build();

    public ConceptReferenceTerm conceptReferenceTermForEdit = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("J19." + id)
            .name("Viral pneumonia " + id + " Changed")
            .source(CONCEPT_SOURCE)
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.: Changed \n" +
                    id)
            .version("1.1")
            .build();

    public ConceptReferenceTerm conceptReferenceTermForFinding = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("M54." + id)
            .name("Back pain " + id)
            .source(CONCEPT_SOURCE)
            .description("Lower Back Pain ")
            .version("1.0")
            .build();


    public Concept conceptForFindingEdit = new Concept.ConceptBuilder()
            .name("Back Pain " + id)
            .synonyms1("Back Pain " + id + " Syn Changed")
            .synonyms2("Back Pain " + id + " Syn2")
            .shortName("BackPain" + id + " Changed")
            .description("Back Pain" + id)
//            .conceptClass("Finding")
            .conceptClass("Symptom")
//            .conceptClass("Symptom/Finding")
            .dataType("N/A")
            .version("1.1")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTermForFinding.getCode())
            .conceptMappingName(conceptReferenceTermForFinding.getName())
            .build();

    public Concept conceptForFinding = new Concept.ConceptBuilder()
            .name("Back Pain " + id)
            .synonyms1("Back Pain " + id + " Syn")
            .synonyms2("Back Pain " + id + " Syn2")
            .shortName("BackPain" + id)
            .description("Back Pain" + id)
//            .conceptClass("Finding")
            .conceptClass("Symptom")
//            .conceptClass("Symptom/Finding")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTermForFinding.getCode())
            .conceptMappingName(conceptReferenceTermForFinding.getName())
            .build();

    public ConceptReferenceTerm conceptReferenceTermForNumericConcept = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("T87273009-" + id)
            .name("Temperature normaL " + id)
            .source("SNOMED-BD [SNOMED-BD]")
            .description("Normal Body Temperature  (finding) ")
            .version("1.0")
            .build();

    public Concept conceptForNumericConcept = new Concept.ConceptBuilder()
            .name("Temperature normal " + id)
            .synonyms1("Temperature normal " + id + " Syn")
            .synonyms2("Temperature normal " + id + " Syn2")
            .shortName("BodyTemperature" + id)
            .description("Normal Body Temperature  (finding) " + id)
            .conceptClass("Misc")
            .dataType("Numeric")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("SNOMED-BD")
            .conceptMappingCode(conceptReferenceTermForNumericConcept.getCode())
            .conceptMappingName(conceptReferenceTermForNumericConcept.getName())
            .numericAbsoluteHigh("102.5")
            .numericCriticalHigh("101.3")
            .numericNormalHigh("100.2")
            .numericNormalLow("98.0")
            .numericCriticalLow("97.2")
            .numericAbsoluteLow("96.5")
            .build();

    public Concept conceptForNumericConceptEdit = new Concept.ConceptBuilder()
            .name("Temperature normal " + id)
            .synonyms1("Temperature normal " + id + " Syn Changed")
            .synonyms2("Temperature normal " + id + " Syn2 Changed ")
            .shortName("BodyTemperature" + id)
            .description("Normal Body Temperature  (finding) " + id)
            .conceptClass("Misc")
            .dataType("Numeric")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("SNOMED-BD")
            .conceptMappingCode(conceptReferenceTermForNumericConcept.getCode())
            .conceptMappingName(conceptReferenceTermForNumericConcept.getName())
            .numericAbsoluteHigh("102.1")
            .numericCriticalHigh("101.2")
            .numericNormalHigh("100.0")
            .numericNormalLow("98.1")
            .numericCriticalLow("97.1")
            .numericAbsoluteLow("96.6")
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
            .source(CONCEPT_SOURCE)
            .description("Viral pneumonia, unspecified\n" +
                    "Excl.:\n" +
                    id2)
            .version("1.0")
            .build();


    public ConceptReferenceTerm referenceTermForEncounterSync = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("S40")
            .name("Superficial injury of shoulder and upper arm")
            .source(CONCEPT_SOURCE)
            .description("Superficial injury of shoulder and upper arm")
            .version("1.0")
            .build();

    public Concept conceptWithReferenceTermForEncounterSync = new Concept.ConceptBuilder()
            .name("Superficial injury of shoulder and upper arm")
            .synonyms1("Shoulder Injury")
            .synonyms2("Upper Arm pneumonia")
            .shortName("Superficial shoulder injury")
            .description("Superficial injury of shoulder and upper arm ")
            .conceptClass("Diagnosis")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(referenceTermForEncounterSync.getCode())
            .conceptMappingName(referenceTermForEncounterSync.getName())
            .build();

    public Concept conceptWithOutReferenceTermForEncounterSync = new Concept.ConceptBuilder()
            .name("Fracture in upper arm")
            .synonyms1("Arm Fracture")
            .synonyms2("Hand Fracture")
            .shortName("Arm Fracture")
            .description("Fracture in upper arm ")
            .conceptClass("Diagnosis")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .build();

    public ConceptReferenceTerm referenceTermForChiefComplainSync = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("R07")
            .name("Pain in throat and chest")
            .source(CONCEPT_SOURCE)
            .description("Pain in throat and chest")
            .version("1.0")
            .build();

    public Concept conceptWithReferenceTermForChiefComplainSync = new Concept.ConceptBuilder()
            .name("Pain in throat and chest")
            .synonyms1("Throat and chest pain")
            .synonyms2("Chest and throat pain")
            .shortName("chest pain")
            .description("Pain in throat and chest")
            .conceptClass("Finding")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(referenceTermForChiefComplainSync.getCode())
            .conceptMappingName(referenceTermForChiefComplainSync.getName())
            .build();

    public Concept conceptWithOutReferenceTermForChiefComplainSync = new Concept.ConceptBuilder()
            .name("Other chest pain")
            .synonyms1("Other chest pain1")
            .synonyms2("Other chest pain2")
            .shortName("OtherChestPain")
            .description("Other chest pain")
            .conceptClass("Symptom")
            .dataType("N/A")
            .version("1.0")
            .build();

    private String id3 = "101";

    public ConceptReferenceTerm conceptReferenceTermForValueSetData = new ConceptReferenceTerm.ConceptReferenceTermBuilder()
            .code("VSCode" + id3)
            .name("VSName " + id3)
            .source(CONCEPT_SOURCE)
            .description("Description" +id3)
            .version("1.0")
            .build();


    public Concept conceptForValueSetData = new Concept.ConceptBuilder()
            .name("VS Value" + id3)
            .synonyms1("VS " + id3 + " Syn")
            .synonyms2("VS " + id3 + " Syn2")
            .shortName("VS" + id3)
            .description("VS Desc "+ id3)
            .conceptClass("Misc")
            .dataType("N/A")
            .version("1.0")
            .conceptMappingRelationship("SAME-AS")
            .conceptMappingSource("ICD10-BD")
            .conceptMappingCode(conceptReferenceTermForValueSetData.getCode())
            .conceptMappingName(conceptReferenceTermForValueSetData.getName())
            .build();


}
