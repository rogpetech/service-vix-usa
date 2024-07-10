/* 
 * ===========================================================================
 * File Name JobMapper.java
 * 
 * Created on Aug 5, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: JobMapper.java,v $
 * ===========================================================================
 */
package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.service.vix.dto.JobDTO;
import com.service.vix.models.Job;

/**
 * This class is used to map Job to JobDTO and Vies Versa
 */
@Mapper(componentModel = "spring")
public interface JobMapper {

	JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

	/**
	 * This method is used to convert Job to JobDTO
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 5, 2023
	 * @param job
	 * @return
	 */
	JobDTO jobToJobDTO(Job job);

	/**
	 * This method is used to convert JobDTO to Job
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 5, 2023
	 * @param jobDTO
	 * @return
	 */
	Job jobDTOToJob(JobDTO jobDTO);

}
