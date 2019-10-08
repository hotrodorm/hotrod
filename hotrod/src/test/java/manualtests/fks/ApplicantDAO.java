package manualtests.fks;

import java.util.List;

public class ApplicantDAO {

  private static final String SELECT_APPLICANT_MAPPER = "applicant_mapper";
  private static final String SELECT_APPLICANTS_MAPPER = "applicants_mapper";
  private static final String SELECT_PERSON_MAPPER = "person_mapper";
  private static final String SELECT_PERSONS_MAPPER = "persons_mapper";

  // === Select PARENT ===

  public ParentApplicantSelector selectParentApplicant() {
    return new ParentApplicantSelector();
  }

  public ParentPersonSelector selectParentPerson() {
    return new ParentPersonSelector();
  }

  public class ParentApplicantSelector {

    public ParentApplicantSelectorOnId onId() {
      return new ParentApplicantSelectorOnId();
    }

    public ParentApplicantSelectorOnId onSid() {
      return new ParentApplicantSelectorOnId();
    }

    public class ParentApplicantSelectorOnId {

      public ApplicantVO byReferrerId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setId(vo.getReferrerId());
        return retrieveApplicantSQL(SELECT_APPLICANT_MAPPER, filter);
      }

      public ApplicantVO byFriendId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setId(vo.getFriendId());
        return retrieveApplicantSQL(SELECT_APPLICANT_MAPPER, filter);
      }

    }

    public class ParentApplicantSelectorOnSid {

      public ApplicantVO byReferrerId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setSid(vo.getReferrerId());
        return retrieveApplicantSQL(SELECT_APPLICANT_MAPPER, filter);
      }

    }

  }

  public class ParentPersonSelector {

    public ParentPersonSelectorOnId onId() {
      return new ParentPersonSelectorOnId();
    }

    public ParentPersonSelectorOnSid onSid() {
      return new ParentPersonSelectorOnSid();
    }

    public class ParentPersonSelectorOnId {

      public PersonVO byReferrerId(final ApplicantVO vo) {
        AbstractPersonVO filter = new AbstractPersonVO();
        filter.setId(vo.getReferrerId());
        return retrievePersonSQL(SELECT_PERSON_MAPPER, filter);
      }

    }

    public class ParentPersonSelectorOnSid {

      public PersonVO byReferrerId(final ApplicantVO vo) {
        AbstractPersonVO filter = new AbstractPersonVO();
        filter.setSid(vo.getReferrerId());
        return retrievePersonSQL(SELECT_PERSON_MAPPER, filter);
      }

    }

  }

  // === Select CHILDREN ===

  public ChildrenApplicantSelector selectChildrenApplicant() {
    return new ChildrenApplicantSelector();
  }

  public ChildrenPersonSelector selectChildrenPerson() {
    return new ChildrenPersonSelector();
  }

  public class ChildrenApplicantSelector {

    public ChildrenApplicantSelectorOnId onId() {
      return new ChildrenApplicantSelectorOnId();
    }

    public ChildrenApplicantSelectorOnSid onSid() {
      return new ChildrenApplicantSelectorOnSid();
    }

    public class ChildrenApplicantSelectorOnId {

      public List<ApplicantVO> byReferrerId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setId(vo.getReferrerId());
        return retrieveApplicantsSQL(SELECT_APPLICANTS_MAPPER, filter);
      }

      public List<ApplicantVO> byFriendId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setId(vo.getFriendId());
        return retrieveApplicantsSQL(SELECT_APPLICANTS_MAPPER, filter);
      }

    }

    public class ChildrenApplicantSelectorOnSid {

      public List<ApplicantVO> byReferrerId(final ApplicantVO vo) {
        AbstractApplicantVO filter = new AbstractApplicantVO();
        filter.setSid(vo.getReferrerId());
        return retrieveApplicantsSQL(SELECT_APPLICANTS_MAPPER, filter);
      }

    }

  }

  public class ChildrenPersonSelector {

    public ChildrenPersonSelectorOnId onId() {
      return new ChildrenPersonSelectorOnId();
    }

    public ChildrenPersonSelectorOnSid onSid() {
      return new ChildrenPersonSelectorOnSid();
    }

    public class ChildrenPersonSelectorOnId {

      public List<PersonVO> byReferrerId(final ApplicantVO vo) {
        AbstractPersonVO filter = new AbstractPersonVO();
        filter.setId(vo.getReferrerId());
        return retrievePersonsSQL(SELECT_PERSONS_MAPPER, filter);
      }

    }

    public class ChildrenPersonSelectorOnSid {

      public List<PersonVO> byReferrerId(final ApplicantVO vo) {
        AbstractPersonVO filter = new AbstractPersonVO();
        filter.setSid(vo.getReferrerId());
        return retrievePersonsSQL(SELECT_PERSONS_MAPPER, filter);
      }

    }

  }

  // Helper/Utilities

  private ApplicantVO retrieveApplicantSQL(final String mapper, final AbstractApplicantVO filter) {
    return null;
  }

  private List<ApplicantVO> retrieveApplicantsSQL(final String mapper, final AbstractApplicantVO filter) {
    return null;
  }

  private PersonVO retrievePersonSQL(final String mapper, final AbstractPersonVO filter) {
    return null;
  }

  private List<PersonVO> retrievePersonsSQL(final String mapper, final AbstractPersonVO filter) {
    return null;
  }

}
