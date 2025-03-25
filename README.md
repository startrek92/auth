Converting a Python-Based Authentication Server to Java Spring Boot: A Project from My Experience


Spring Security Architecture:

Servlet Container -> Filter Chains (F1 -> F2 -> (Security Filter Chain) -> Servlets (Dispatcher Servlets) -> Interceptors -> Controller

- Controller has business logic

Security Filter Chains:
- For each authentication methods different filters are there
- ex BasicAuthenticationFilter, UsernamePasswordAuthenticationFilter


Security Filter Chain -> AuthenticationManager -> ProviderManager (has info which authentication type is used, will send request to appropriate provider) -> AuthenticationProvider (Interface) ex DaoAuthenticationProvider

DaoAuthenticationProvider -> UserDetailsService (Interface)

UserDetailsService has two implementations
- inMemoryUser
- JDBC User

- Once User ia authenticated Security Filter stores authentication info in  Security Context
- Controller can access Security Context



