package tw.com.firstbank.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.jgroups.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.firstbank.InwardRmtApp;
import tw.com.firstbank.model.InwardRmtDto;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(classes = InwardRmtApp.class)
@ActiveProfiles("test")
@Sql({"/schema.sql", "/data.sql"})
public class InwardRmtServiceTest {
  
  @Autowired
  private RestTemplate restTemplate;
  
  @Autowired
  private InwardRmtService service ;
  
  @Autowired
  private RepositoryHelper repoHelper;
  
  @Value("${aml-url:http://localhost:8090/aml/v1/screen}")
  private String amlUrl;
  
  private MockRestServiceServer mockServer;
  private ObjectMapper mapper = new ObjectMapper();
  
  private InwardRmtDto generateDto() {
    InwardRmtDto dto = null;
    
    dto = new InwardRmtDto();
    dto.setId(UUID.randomUUID().toString());
    dto.setTxnRefNo("CA200521226481");
    dto.setBankOpCode("CRED");
    dto.setValueDate(20200522);
    dto.setCcy("USD");
    dto.setOrderCust("/065324003877 TEST 3993 MARK");
    dto.setBenefCust("/62610123456\\nANDY CO.\\nNO.33.\\nTAINAN");
    dto.setBenefAcct("62610123456");
    dto.setSenderCorr("CHASUS33");
    dto.setReceiverCorr("BKTRUS33");
    dto.setDetailCharge("SHA");
    return dto;
  }
  
  private String encodeValue(String value) throws UnsupportedEncodingException {
    return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
  }
  
  private Integer mockAmlStatus(String name, Integer status) throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
    Integer amlStatus = status;
    
    mockServer.expect(ExpectedCount.once(), 
        requestTo(new URI(amlUrl+"?name="+encodeValue(name))))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(mapper.writeValueAsString(amlStatus))
      );  
    return status;
  }
  
  @Before
  public void init() {
      mockServer = MockRestServiceServer.createServer(restTemplate);
  }
  
  // 案例一電文-成功
  @Test
  public void testCase1() throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
    InwardRmtDto dto = null;
    
    mockAmlStatus("帳號一", 0);
    
    dto = generateDto();
    dto.setInstAmt(123);
    
    System.out.println(">>>" + dto.toString());
    
    dto = service.processInwardRmt(dto);     
    
    Assert.assertTrue(dto.getReplyStatus() == 1);
  }
  
  // 案例三電文-洗防檢核
  @Test
  public void testCase3() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
    InwardRmtDto dto = null;
    
    mockAmlStatus("帳號一", 1);
    
    dto = generateDto();
    dto.setInstAmt(123);
    
    System.out.println(">>>" + dto.toString());
    
    dto = service.processInwardRmt(dto);     
    
    Assert.assertTrue(dto.getReplyStatus() == 1);
    
    Assert.assertTrue(repoHelper.findVerifyPendingInwardRmt().size() > 0);
  }

  // 案例四電文-匯入資料預處理異常
  @Test
  public void testCase4() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
    InwardRmtDto dto = null;
    
    mockAmlStatus("帳號一", 0);
    
    dto = generateDto();
    dto.setInstAmt(123);
    dto.setBenefCust("/62610123456 " + StringUtils.repeat("A", 40));
    dto = service.processInwardRmt(dto);    
    Assert.assertTrue(dto.getReplyStatus() == 0);
   
  }
  
  // 案例五電文-解款異常
  @Test
  public void testCase5() throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
    InwardRmtDto dto;
    
    mockAmlStatus("帳號一", 0);
    
    dto = generateDto();
    dto.setInstAmt(123);
    dto.setCcy("NZD");
    dto = service.processInwardRmt(dto);    

    Assert.assertTrue(dto.getReplyStatus() == 1);
    Assert.assertTrue(repoHelper.findVerifiedInwardRmt().size() > 0);
  }
  
}
