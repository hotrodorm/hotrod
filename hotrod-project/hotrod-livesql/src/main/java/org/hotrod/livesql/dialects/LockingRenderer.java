package org.hotrod.livesql.dialects;

import org.hotrod.livesql.queries.select.AbstractSelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.AbstractSelectObject.LockingMode;

public interface LockingRenderer {

  String renderLockingAfterFromClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

  String renderLockingAfterLimitClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

}
