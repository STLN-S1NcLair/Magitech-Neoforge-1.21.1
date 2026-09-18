package net.stln.magitech.api.machine.inspection;

@FunctionalInterface
public interface MachineInspectionDataProvider {
    void append(MachineInspectionContext context, MachineInspectionData.Builder builder);
}
