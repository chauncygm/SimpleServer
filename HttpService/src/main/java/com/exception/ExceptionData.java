package com.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExceptionData {

    private List<String> errorMsgList = new ArrayList<>(1);

    private void addErrorMsg(String errorMsg) {
        errorMsgList.add(errorMsg);
    }

    public static ExceptionDataBuilder builder() {
        return new ExceptionDataBuilder();
    }

    public static class ExceptionDataBuilder {

        private final ExceptionData exceptionData = new ExceptionData();

        public void error(String errorMsg) {
            exceptionData.addErrorMsg(errorMsg);
        }

        public ExceptionData build() {
            return exceptionData;
        }

    }
}
