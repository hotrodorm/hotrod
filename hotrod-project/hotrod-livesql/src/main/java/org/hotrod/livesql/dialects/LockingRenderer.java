package org.hotrod.livesql.dialects;

import org.hotrod.livesql.queries.select.UnarySelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingMode;

public interface LockingRenderer {

  String renderLockingAfterFromClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

  String renderLockingAfterLimitClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

}
