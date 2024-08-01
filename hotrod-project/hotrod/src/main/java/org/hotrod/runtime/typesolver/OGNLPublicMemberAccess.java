package org.hotrod.runtime.typesolver;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Map;

import ognl.MemberAccess;

public class OGNLPublicMemberAccess implements MemberAccess {

  /**
   * Returns true if the given member is accessible or can be made accessible by
   * this object.
   *
   * @param context      the current execution context (not used).
   * @param target       the Object to test accessibility for (not used).
   * @param member       the Member to test accessibility for.
   * @param propertyName the property to test accessibility for (not used).
   * @return true if the member is accessible in the context, false otherwise.
   */
  public boolean isAccessible(final @SuppressWarnings("rawtypes") Map context, final Object target, final Member member,
      final String propertyName) {
//    log.debug(
//        "[isAccessible] context=" + context + " target=" + target + " member=" + member + " property=" + propertyName);
    return Modifier.isPublic(member.getModifiers());
  }

  /**
   * Sets the member up for accessibility
   * 
   * @param context      the current execution context.
   * @param target       the Object upon which to perform the setup operation.
   * @param member       the Member upon which to perform the setup operation.
   * @param propertyName the property upon which to perform the setup operation.
   * @return the Object representing the original accessibility state of the
   *         target prior to the setup operation.
   */
  @Override
  public Object setup(final @SuppressWarnings("rawtypes") Map context, final Object target, final Member member,
      final String propertyName) {
//    log.debug("[setup] context=" + context + " target=" + target + " member=" + member + " property=" + propertyName);
    return null;
  }

  /**
   * Restores the member from the previous setup call.
   * 
   * @param context      the current execution context.
   * @param target       the Object upon which to perform the setup operation.
   * @param member       the Member upon which to perform the setup operation.
   * @param propertyName the property upon which to perform the setup operation.
   * @param state        the Object holding the state to restore (target state
   *                     prior to the setup operation).
   */

  @Override
  public void restore(final @SuppressWarnings("rawtypes") Map context, final Object target, final Member member,
      String propertyName, Object state) {
//    log.debug("[restore] context=" + context + " target=" + target + " member=" + member + " property=" + propertyName);
  }

}
