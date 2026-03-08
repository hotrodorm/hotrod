package org.hotrod.livesql.dialects;

import org.hotrod.livesql.queries.select.FlatSelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.FlatSelectObject.LockingMode;

public interface LockingRenderer {

  String renderLockingAfterFromClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

  String renderLockingAfterLimitClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency, Number waitTime);

}
