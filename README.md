# Product Management Backend

Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, seguindo o padrão MVC. Ele está atualmente em desenvolvimento e visa fornecer funcionalidades para o gerenciamento de produtos e categorias, bem como a autenticação de usuários utilizando JWT.

## Estrutura do Projeto

O projeto está organizado nas seguintes pastas principais:

- **config**: Configurações do projeto, incluindo segurança.
- **controllers**: Controladores que lidam com as requisições HTTP.
- **domain**: Classes de domínio que representam as entidades do banco de dados.
- **repositories**: Interfaces de repositório para acesso ao banco de dados.
- **services**: Serviços que contêm a lógica de negócios.

## Configuração de Segurança

### CorsConfig
```java
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Permitir apenas requisições deste domínio
                        .allowedMethods("GET", "POST", "OPTIONS", "PUT") // Métodos HTTP permitidos
                        .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers") // Cabeçalhos permitidos
                        .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials") // Cabeçalhos expostos
                        .allowCredentials(true) // Permitir credenciais (cookies, autenticação)
                        .maxAge(3600); // Tempo máximo de cache para preflight requests (em segundos)
            }
        };
    }
}
```

### SecurityConfig

Configurações de segurança utilizando Spring Security.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
    }
}
```

### SecurityFilter

```java
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null){
            var username = tokenService.validateToken(token);
            UserDetails user = userRepository.findByUsername(username);

            if(user != null){
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer", "").trim();
    }
}
```
## Configuração do Banco de Dados
`application.properties`
```
spring.application.name=product-management

server.port=8090

spring.profiles.active=${APP_PROFILE:test}
spring.jpa.open-in-view=false
```
`application-test.properties`

Configuração para o banco de dados H2 em ambiente de teste:
```
# H2 Connection
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# H2 Client
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Show SQL
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

api.security.token.secret=${JWT_Secret:JWTSecret}
```
## Instalação e Execução
1. Clone o repositório:
```
git clone https://github.com/thisd92/prodMngmtBackend.git
```
2. Navegue até o diretório do projeto:
```
cd prodMngmtBackend
```
3. Execute a aplicação
```
./mvnw spring-boot:run
```

## Contribuição

Se você deseja contribuir com este projeto, por favor, siga as etapas abaixo:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`).
3. Comite suas alterações (`git commit -m 'Adiciona minha feature'`).
4. Faça um push para a branch (`git push origin feature/MinhaFeature`).
5. Abra um Pull Request.
   
## Licença

Este projeto está licenciado sob a MIT License.
