package dev.harastw.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.harastw.wallet.dto.WalletOperationRequest;
import dev.harastw.wallet.model.Wallet;
import dev.harastw.wallet.repository.WalletRepository;

@SpringBootTest
@AutoConfigureWebMvc
class WalletControllerTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    static {
        postgres.start();
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private WalletRepository walletRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        walletRepository.deleteAll();
    }

    @Test
    void testDepositAndWithdraw() throws Exception {
        var walletId = java.util.UUID.randomUUID();
        walletRepository.save(new Wallet(walletId));

        var deposit = new WalletOperationRequest();
        deposit.setValletId(walletId);
        deposit.setOperationType(WalletOperationRequest.OperationType.DEPOSIT);
        deposit.setAmount(java.math.BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1000.00"));

        var withdraw = new WalletOperationRequest();
        withdraw.setValletId(walletId);
        withdraw.setOperationType(WalletOperationRequest.OperationType.WITHDRAW);
        withdraw.setAmount(java.math.BigDecimal.valueOf(500));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("500.00"));
    }
}