package com.practica.producto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.practica.producto.service.UserDetailsServiceImp;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity  //habilita el uso de anotaciones para el filtro en la clase controller 
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//            return httpSecurity
//                    .csrf(csrf -> csrf.disable()) // pq no usamos formulario
//                    //sin estado: no guardaremos las sesiones en memoria pq son pesadas y manejaremos token
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .httpBasic(Customizer.withDefaults()) //para que solo pida user y password
//                    .authorizeHttpRequests(http -> {
//                        //configurar los endpoints publicos
//                        http.requestMatchers(HttpMethod.GET, "/auth/hello").permitAll();
//
//                        //configurar los endpoints privados
//                        http.requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAnyAuthority("CREATE");
//
//                        //configurar los endpoint no especificados
//                        http.anyRequest().denyAll();  // deniega el acceso a todos
//                        //http.anyRequest().authenticated(); // permite el acceso a los autenticados
//                    })
//                    .build();
//    }

    //Lo dejamos a si porque vamos a usar anotaciones en el controller que va hacer lo mismo
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Configurar los endpoints publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/get").permitAll();

                    // Cofnigurar los endpoints privados
                    http.requestMatchers(HttpMethod.POST, "/auth/post").hasAnyRole("ADMIN", "DEVELOPER");
                    http.requestMatchers(HttpMethod.PATCH, "/auth/patch").hasAnyAuthority("REFACTOR");

                    // Configurar el resto de endpoint - NO ESPECIFICADOS
                    http.anyRequest().denyAll();
                })
                .build();
    }

    /*
        El AuthenticationManager recibe la configuracion de autenticacion(AuthenticationConfiguration)
        luego tiene que obtener el administrador de autenticacion (getAuthenticationManager)
    */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
        Vamos a usar el DaoAuthenticationProvider de los mucho que hay, por que nos va permitir
        conectar con la bd. Y necesita 2 componentes mas que son el PassWordEncoder y el UserDetailService
    */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImp userDetailsService){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
/**
    // configurando el UserDetailsService para un usuario
    //@Bean
    public UserDetailsService userDetailsService(){
        // spring security valida a los usuario mediante el UserDetails
        UserDetails userDetails = User.withUsername("Jorge")
                .password("1234")
                .roles("ADMIN") // ROL
                .authorities("READ", "CREATE")  // PERMISOS
                .build();
        // guardamos el usario creado en memoria(LO NORMAL ES OBTENER LOS USER DESDE LA BD)
        return new InMemoryUserDetailsManager(userDetails);
    }
**/

// configurando el UserDetailsService para varios usuarios en memoria
    /*     @Bean
    public UserDetailsService userDetailsService(){
        // spring security valida a los usuario mediante el UserDetails
        List<UserDetails> userDetails = new ArrayList<>();
    
        userDetails.add(User.withUsername("Jorge")
                .password("1234")
                .roles("ADMIN") // ROL
                .authorities("READ", "CREATE")  // PERMISOS
                .build());
    
        userDetails.add(User.withUsername("Luis")
                .password("1234")
                .roles("USER") // ROL
                .authorities("READ")  // PERMISOS
                .build());
    
    
        // guardamos el usario creado en memoria(LO NORMAL ES OBTENER LOS USER DESDE LA BD)
        return new InMemoryUserDetailsManager(userDetails);
    }
    */
    

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();  // encripta la contraseña
    }
}
