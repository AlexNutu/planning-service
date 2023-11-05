package com.alenut.planningservice.common.dto;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import lombok.Getter;

@Getter
public class ListResultDto<T> {

  private List<T> elements;

  public int size() {
    if (isEmpty(elements)) {
      return 0;
    }

    return elements.size();
  }

  public void setElements(List<T> elements) {
    this.elements = elements;
  }
}
