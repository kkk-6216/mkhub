package com.pro.file_storage_service.decoder;

import com.pro.file_storage_service.exception.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            return new UnauthorizedException("Invalid token");
        }
        return defaultDecoder.decode(methodKey, response);
    }

}
