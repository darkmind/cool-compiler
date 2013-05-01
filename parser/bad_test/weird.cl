class Main inherits IO {
    main() : SELF_TYPE {
        {
                case
                        case avar of
                                (* missing ID *)
                         : C => avar <- c.method6(c.value());
                        a : A => avar <- a.method3(a.value());
                        o : Object => {
                                out_string("Oooops\n");
                                0;
                                };
        esac of
                        a : A =>       
                                case avar of
                                c : C => avar <- c.method6(c.value());
                                a : A => avar <- a.method3(a.value());
                                o : Object => {
                                        out_string("Oooops\n");
                                        0;
                                        };
                esac;
                        b : B => 1;
                (* no esac *)  
        }
    };
};
