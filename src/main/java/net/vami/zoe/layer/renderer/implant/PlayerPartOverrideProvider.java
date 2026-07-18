package net.vami.zoe.layer.renderer.implant;

import java.util.EnumSet;

public interface PlayerPartOverrideProvider {

    void addParts(int implantCount, EnumSet<PlayerModelPart> parts);
}
