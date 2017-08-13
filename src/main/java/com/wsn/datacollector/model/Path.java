package com.wsn.datacollector.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lufen on 2017/8/6.
 */
public class Path {

    private Long pathId;

    List<FingerprintDecorator> fingerprints = new LinkedList<>();

    public Long getPathId() {
        return pathId;
    }

    public void setPathId(Long pathId) {
        this.pathId = pathId;
    }

    public List<FingerprintDecorator> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(List<FingerprintDecorator> fingerprints) {
        this.fingerprints = fingerprints;
    }

    public Path(Long pathId, List<FingerprintDecorator> fingerprints) {
        this.pathId = pathId;
        this.fingerprints = fingerprints;
    }

    @Override
    public String toString() {
        return "Path{" +
                "pathId=" + pathId +
                ", fingerprints=" + fingerprints +
                '}';
    }
}
