package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._TempDir;

import java.io.Serializable;

public class TempDir extends _TempDir implements Serializable {

    public TempDirFile getFile(String filename) {
        for (TempDirFile tempDirFile : getFiles()) {
            if (tempDirFile.getFileName().equals(filename)) {
                return tempDirFile;
            }
        }
        return null;
    }
}
