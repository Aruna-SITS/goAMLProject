package com.itechro.iaml.model.ctr;
import java.util.ArrayList;
import java.util.List;

public class InvolvedPartyDTO {
	List<PartyDTO> listParty = new ArrayList<PartyDTO>();

	public List<PartyDTO> getListParty() {
		return listParty;
	}

	public void setListParty(List<PartyDTO> listParty) {
		this.listParty = listParty;
	}
}
