/* 
 * ===========================================================================
 * File Name StaffMapper.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.StaffDTO;
import com.service.vix.models.Staff;

@Mapper(componentModel = "spring")
public interface StaffMapper {

	StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

	/**
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return StaffDTO
	 * @param staff
	 * @return
	 * @exception Description
	 */
	StaffDTO staffToStaffDTO(Staff staff);

	/**
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return Staff
	 * @param staffDTO
	 * @return
	 * @exception Description
	 */
	Staff staffDtoTOStaff(StaffDTO staffDTO);
}
