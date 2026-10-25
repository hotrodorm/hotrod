package org.hotrod.torcs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TorcsTest {

  private Torcs torcs;

  @AfterEach
  void stopScheduler() {
    if (torcs != null) {
      torcs.shutdown();
    }
  }

  @Test
  void canBeActivatedAndDeactivated() {
    torcs = new Torcs();

    torcs.deactivate();
    assertFalse(torcs.isActive());
    torcs.activate();
    assertTrue(torcs.isActive());
  }

  @Test
  void validatesResetScheduleBounds() {
    torcs = new Torcs();

    assertThrows(RuntimeException.class, () -> torcs.setResetSchedule(-1, 60000));
    assertThrows(RuntimeException.class, () -> torcs.setResetSchedule(0, 59999));
    assertThrows(RuntimeException.class, () -> torcs.setResetSchedule(0, 1000L * 60 * 60 * 24 * 367));
  }

  @Test
  void validatesLobPlanSetting() {
    torcs = new Torcs();

    assertFalse(torcs.isLobsAllowedInPlans());
    torcs.allowLOBsInPlans(true);
    assertTrue(torcs.isLobsAllowedInPlans());
  }
}
