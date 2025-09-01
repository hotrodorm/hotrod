package manualtests.fks;

import java.util.List;

public class MyApp {

  public static void main(final String[] args) {

    ApplicantDAO dao = new ApplicantDAO();

    ApplicantVO a = null;

    ApplicantVO b = dao.selectParentApplicant().onId().byReferrerId(a);
    ApplicantVO c = dao.selectParentApplicant().onSid().byReferrerId(a);
    PersonVO d = dao.selectParentPerson().onId().byReferrerId(a);
    PersonVO e = dao.selectParentPerson().onSid().byReferrerId(a);
    ApplicantVO f = dao.selectParentApplicant().onId().byFriendId(a);

    List<ApplicantVO> m = dao.selectChildrenApplicant().onId().byReferrerId(a);
    List<ApplicantVO> n = dao.selectChildrenApplicant().onSid().byReferrerId(a);
    List<PersonVO> o = dao.selectChildrenPerson().onId().byReferrerId(a);
    List<PersonVO> p = dao.selectChildrenPerson().onSid().byReferrerId(a);
    List<ApplicantVO> q = dao.selectChildrenApplicant().onId().byFriendId(a);

  }

}
