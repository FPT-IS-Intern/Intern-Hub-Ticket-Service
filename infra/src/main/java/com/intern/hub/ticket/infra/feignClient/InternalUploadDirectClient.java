package com.intern.hub.ticket.infra.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.intern.hub.library.common.dto.ResponseApi;
import com.intern.hub.ticket.infra.feignClient.dto.DmsDocumentClientModel;

@FeignClient(
  name = "dms",
  url = "${services.dms.url}",
  configuration = { FeignConfiguration.class, FeignMultipartConfig.class }
)
public interface InternalUploadDirectClient {

  @PostMapping(value = "/dms/internal/direct/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseApi<DmsDocumentClientModel> uploadFile(
          @RequestPart("file") MultipartFile file,
          @RequestParam("destinationPath") String destinationPath,
          @RequestParam("actorId") Long actorId
  );
}
