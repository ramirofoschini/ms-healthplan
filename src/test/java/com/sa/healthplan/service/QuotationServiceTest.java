package com.sa.healthplan.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.sa.healthplan.dto.QuoteDTO;
import com.sa.healthplan.dto.QuoteRequest;
import com.sa.healthplan.exception.QuotationException;
import com.sa.healthplan.exception.ResourceNotFoundException;
import com.sa.healthplan.model.AgeBand;
import com.sa.healthplan.model.Customer;
import com.sa.healthplan.model.Dependent;
import com.sa.healthplan.model.HealthPlan;
import com.sa.healthplan.model.PlanPrice;
import com.sa.healthplan.model.Relationship;
import com.sa.healthplan.repository.AgeBandRepository;
import com.sa.healthplan.repository.CustomerRepository;
import com.sa.healthplan.repository.HealthPlanRepository;
import com.sa.healthplan.repository.PlanPriceRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests del motor de tasación (sin base de datos: los repositorios se mockean).
 * Verifica la suma del grupo familiar, la selección de franja por edad y las
 * reglas de negocio que cortan la cotización.
 */
@ExtendWith(MockitoExtension.class)
class QuotationServiceTest {

    @Mock
    private HealthPlanRepository planRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AgeBandRepository ageBandRepository;
    @Mock
    private PlanPriceRepository planPriceRepository;

    @InjectMocks
    private QuotationService service;

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);

    private AgeBand band0to18;
    private AgeBand band36to45;

    @BeforeEach
    void setUp() {
        band0to18 = ageBand(1L, "0-18", 0, 18);
        band36to45 = ageBand(4L, "36-45", 36, 45);
    }

    @Test
    void simulate_sumaTitularYGrupoFamiliar() {
        HealthPlan plan = plan(10L, "Plan 210", true);
        Customer customer = customer(100L, "Juan", "Perez", LocalDate.of(1985, 6, 1)); // 40
        addDependent(customer, Relationship.SPOUSE, "Ana", LocalDate.of(1987, 6, 1));  // 38
        addDependent(customer, Relationship.CHILD, "Leo", LocalDate.of(2015, 6, 1));   // 10

        when(planRepository.findById(10L)).thenReturn(Optional.of(plan));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(ageBandRepository.findAll()).thenReturn(List.of(band0to18, band36to45));
        when(planPriceRepository.findValid(eq(10L), eq(band36to45.getId()), eq(DATE)))
                .thenReturn(List.of(price(new BigDecimal("15000.00"))));
        when(planPriceRepository.findValid(eq(10L), eq(band0to18.getId()), eq(DATE)))
                .thenReturn(List.of(price(new BigDecimal("5000.00"))));

        QuoteDTO quote = service.simulate(new QuoteRequest(10L, 100L, DATE));

        assertThat(quote.lines()).hasSize(3);
        assertThat(quote.lines().get(0).memberType()).isEqualTo("TITULAR");
        assertThat(quote.lines().get(0).age()).isEqualTo(40);
        // 15000 (titular) + 15000 (conyuge) + 5000 (hijo)
        assertThat(quote.total()).isEqualByComparingTo("35000.00");
    }

    @Test
    void simulate_edadEnLimite_usaFranjaCorrecta() {
        HealthPlan plan = plan(10L, "Plan", true);
        Customer customer = customer(100L, "Tin", "Edge", LocalDate.of(2008, 1, 1)); // 18 exactos

        when(planRepository.findById(10L)).thenReturn(Optional.of(plan));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(ageBandRepository.findAll()).thenReturn(List.of(band0to18, band36to45));
        when(planPriceRepository.findValid(10L, band0to18.getId(), DATE))
                .thenReturn(List.of(price(new BigDecimal("5000.00"))));

        QuoteDTO quote = service.simulate(new QuoteRequest(10L, 100L, DATE));

        assertThat(quote.lines().get(0).age()).isEqualTo(18);
        assertThat(quote.lines().get(0).ageBandName()).isEqualTo("0-18");
    }

    @Test
    void simulate_planInactivo_lanzaQuotationException() {
        when(planRepository.findById(10L)).thenReturn(Optional.of(plan(10L, "Plan", false)));

        assertThatThrownBy(() -> service.simulate(new QuoteRequest(10L, 100L, DATE)))
                .isInstanceOf(QuotationException.class);
    }

    @Test
    void simulate_planInexistente_lanzaResourceNotFound() {
        when(planRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.simulate(new QuoteRequest(10L, 100L, DATE)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void simulate_sinPrecioVigente_lanzaQuotationException() {
        HealthPlan plan = plan(10L, "Plan", true);
        Customer customer = customer(100L, "Juan", "Perez", LocalDate.of(1985, 6, 1)); // 40

        when(planRepository.findById(10L)).thenReturn(Optional.of(plan));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(customer));
        when(ageBandRepository.findAll()).thenReturn(List.of(band36to45));
        when(planPriceRepository.findValid(eq(10L), eq(band36to45.getId()), eq(DATE)))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.simulate(new QuoteRequest(10L, 100L, DATE)))
                .isInstanceOf(QuotationException.class);
    }

    // ---- helpers de armado de datos ----

    private HealthPlan plan(Long id, String name, boolean active) {
        HealthPlan plan = new HealthPlan();
        plan.setId(id);
        plan.setName(name);
        plan.setActive(active);
        return plan;
    }

    private Customer customer(Long id, String firstName, String lastName, LocalDate birthDate) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setBirthDate(birthDate);
        return customer;
    }

    private void addDependent(Customer customer, Relationship relationship, String firstName, LocalDate birthDate) {
        Dependent dependent = new Dependent();
        dependent.setRelationship(relationship);
        dependent.setFirstName(firstName);
        dependent.setLastName("Perez");
        dependent.setBirthDate(birthDate);
        customer.addDependent(dependent);
    }

    private AgeBand ageBand(Long id, String name, int ageFrom, int ageTo) {
        AgeBand band = new AgeBand();
        band.setId(id);
        band.setName(name);
        band.setAgeFrom(ageFrom);
        band.setAgeTo(ageTo);
        return band;
    }

    private PlanPrice price(BigDecimal amount) {
        PlanPrice price = new PlanPrice();
        price.setAmount(amount);
        return price;
    }
}
