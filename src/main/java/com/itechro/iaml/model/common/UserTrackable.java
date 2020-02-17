package com.itechro.iaml.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * UserTrackable must implement this interface
 *
 * @author chamara
 */
public interface UserTrackable extends Serializable {

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public Integer getCreatedBy();

	public void setCreatedBy(Integer createdBy);

	public Date getModifiedDate();

	public void setModifiedDate(Date modifiedDate);

	public Integer getModifiedBy();

	public void setModifiedBy(Integer createdBy);
}
