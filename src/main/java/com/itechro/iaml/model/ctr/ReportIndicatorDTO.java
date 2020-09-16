package com.itechro.iaml.model.ctr;

import java.io.Serializable;
import java.util.List;

public class ReportIndicatorDTO implements Serializable {

    private List<String> indicators;

	public List<String> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<String> indicators) {
		this.indicators = indicators;
	}

}
