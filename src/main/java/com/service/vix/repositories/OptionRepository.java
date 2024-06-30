/* 
 * ===========================================================================
 * File Name OptionRepository.java
 * 
 * Created on Jul 20, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: OptionRepository.java,v $
 * ===========================================================================
 */
package com.service.vix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

}
