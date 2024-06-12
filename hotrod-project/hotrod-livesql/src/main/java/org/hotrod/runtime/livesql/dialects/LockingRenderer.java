package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.LockingConcurrency;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.LockingMode;

public interface LockingRenderer {

  String renderLockingAfterFromClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

  String renderLockingAfterLimitClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

}
