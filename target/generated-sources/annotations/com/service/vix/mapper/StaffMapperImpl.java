package com.service.vix.mapper;

import com.service.vix.dto.StaffDTO;
import com.service.vix.models.Staff;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class StaffMapperImpl implements StaffMapper {

    @Override
    public StaffDTO staffToStaffDTO(Staff staff) {
        if ( staff == null ) {
            return null;
        }

        StaffDTO staffDTO = new StaffDTO();

        staffDTO.setId( staff.getId() );
        staffDTO.setAddress( staff.getAddress() );
        staffDTO.setCity( staff.getCity() );
        staffDTO.setState( staff.getState() );
        staffDTO.setSsn( staff.getSsn() );
        staffDTO.setDrivingLicienceNumber( staff.getDrivingLicienceNumber() );
        staffDTO.setJobTitle( staff.getJobTitle() );
        staffDTO.setEmploymentType( staff.getEmploymentType() );
        staffDTO.setDepartment( staff.getDepartment() );
        staffDTO.setManager( staff.getManager() );
        staffDTO.setGroupAssignment( staff.getGroupAssignment() );
        if ( staff.getCreatedAt() != null ) {
            staffDTO.setCreatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( staff.getCreatedAt() ) );
        }
        if ( staff.getUpdatedAt() != null ) {
            staffDTO.setUpdatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( staff.getUpdatedAt() ) );
        }
        staffDTO.setIsPasswordChange( staff.getIsPasswordChange() );
        staffDTO.setCreatedBy( staff.getCreatedBy() );
        staffDTO.setUpdatedBy( staff.getUpdatedBy() );

        return staffDTO;
    }

    @Override
    public Staff staffDtoTOStaff(StaffDTO staffDTO) {
        if ( staffDTO == null ) {
            return null;
        }

        Staff staff = new Staff();

        staff.setId( staffDTO.getId() );
        if ( staffDTO.getCreatedAt() != null ) {
            staff.setCreatedAt( LocalDateTime.parse( staffDTO.getCreatedAt() ) );
        }
        if ( staffDTO.getUpdatedAt() != null ) {
            staff.setUpdatedAt( LocalDateTime.parse( staffDTO.getUpdatedAt() ) );
        }
        staff.setCreatedBy( staffDTO.getCreatedBy() );
        staff.setUpdatedBy( staffDTO.getUpdatedBy() );
        staff.setAddress( staffDTO.getAddress() );
        staff.setCity( staffDTO.getCity() );
        staff.setState( staffDTO.getState() );
        staff.setSsn( staffDTO.getSsn() );
        staff.setDrivingLicienceNumber( staffDTO.getDrivingLicienceNumber() );
        staff.setJobTitle( staffDTO.getJobTitle() );
        staff.setEmploymentType( staffDTO.getEmploymentType() );
        staff.setDepartment( staffDTO.getDepartment() );
        staff.setManager( staffDTO.getManager() );
        staff.setGroupAssignment( staffDTO.getGroupAssignment() );
        staff.setIsPasswordChange( staffDTO.getIsPasswordChange() );

        return staff;
    }
}
