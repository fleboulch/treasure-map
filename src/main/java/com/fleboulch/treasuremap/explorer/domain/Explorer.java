package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.map.domain.HorizontalAxis;
import com.fleboulch.treasuremap.map.domain.VerticalAxis;

public class Explorer {

    private final Name name;
    private final HorizontalAxis x;
    private final VerticalAxis y;
    private final Orientation orientation;
    private final Movements movements;


    public Explorer(Name name, HorizontalAxis x, VerticalAxis y, Orientation orientation, Movements movements) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.movements = movements;
    }

    public Name name() {
        return name;
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }

    public Orientation orientation() {
        return orientation;
    }

    public Movements movements() {
        return movements;
    }

}
