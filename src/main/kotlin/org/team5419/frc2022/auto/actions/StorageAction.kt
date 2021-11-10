package org.team5419.frc2022.auto.actions

import org.team5419.frc2022.subsystems.Storage.StorageMode
import org.team5419.frc2022.subsystems.Storage
import org.team5419.frc2022.fault.auto.Action

public class PassiveStorageAction(): ConfigStorageAction(StorageMode.PASSIVE)
public class LoadStorageAction(): ConfigStorageAction(StorageMode.LOAD)
public class DisableStorageAction(): ConfigStorageAction(StorageMode.OFF)

open class ConfigStorageAction(val storage: StorageMode): Action() {
    override fun start() {
        Storage.mode = storage
    }

    override fun next() = true

}
