package com.alenut.planningservice.dto;

import com.alenut.planningservice.common.dto.ListResultDto;
import java.util.List;

public class ShiftListDto extends ListResultDto<ShiftDto> {

  public static ShiftListDto of(List<ShiftDto> elements) {
    ShiftListDto result = new ShiftListDto();
    result.setElements(elements);
    return result;
  }
}
