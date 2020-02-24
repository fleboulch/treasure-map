package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.PlainsBox;

public class BoxIsOutOfMapException extends RuntimeException {

    public BoxIsOutOfMapException(PlainsBox box, Dimension dimension) {
        super(String.format("'%s' is out of %s", box, dimension));
    }

}
