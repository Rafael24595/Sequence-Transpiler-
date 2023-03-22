package resources;

public class KInput {
    
    public static final String SEQUENCE_TEST_1 = """
        FIELD $object_data_1 MUTATE OBJECT
            FIELD $integer INCREMENT 11.95
            FIELD $string MUTATE "str 2"
            FIELD $boolean MUTATE false
        CLOSE
        FIELD $list_data MUTATE LIST
            10 11 12 13
        CLOSE
        FIELD $complex_list_data AS LIST
            OBJECT
                FIELD $integer AS 311
                FIELD $string AS "str 3"
            CLOSE
        CLOSE
        FIELD $erase DELETE
        FIELD object_data_complex_1 MUTATE OBJECT
            FIELD $integer AS 4
        CLOSE
        FIELD $object_data_complex_2 AS OBJECT
            FIELD $sub_object_data_complex_2 AS OBJECT
                FIELD $integer AS 12
                FIELD $string AS "str 5"
                FIELD $boolean AS true
            CLOSE
        CLOSE
            """;

    public static final String BASE_TEST_1 = """
            {
                "object_data_1":{
                  "integer": 1,
                  "string": "empty 1",
                  "boolean": true,
                  "string_new": "str 1"
                },
                "list_data": [0],
                "erase": "this_field_out",
                "object_data_complex_1":{
                  "integer": 3,
                  "sub_object_data_complex_1": {
                      "string": "str 4"
                  }
                }
              }
                """;

    public static final String BASE_TEST_2 = """
            {
                "1_erase_integer": 1,
                "1_eq_object": {
                    "2_eq_string": "string_5",
                    "2_eq_number": 4
                },
                "1_eq_number": 1,
                "1_mu_string": "string",
                "1_eq_boolean": false,
                "1_mu_list": [
                    1, 2, 3, 4
                ],
                "1_mu_object": {
                    "2_eq_string": "string_2",
                    "2_mu_object":{
                        "3_eq_number": 2,
                        "3_mu_object":{
                            "4_mu_string": "string_4",
                            "4_mu_integer_1": 1,
                            "4_mu_integer_2": 3,
                            "4_eq_boolean": false
                        }
                    }
                }
            }
            """;

    public static final String UPDATE_TEST_1 = """
            {
                "1_eq_number": 1,
                "1_eq_object": {
                    "2_eq_string": "string_5",
                    "2_eq_number": 4
                },
                "1_mu_string": "string_x",
                "1_eq_boolean": false,
                "1_mu_list": [
                    1, 2, "3", {
                        "2_as_string": "string_x",
                        "2_as_boolean": false
                    }
                ],
                "1_mu_object": {
                    "2_eq_string": "string_2",
                    "2_mu_object":{
                        "3_eq_number": 2,
                        "3_mu_object":{
                            "4_mu_string": "string_z",
                            "4_mu_integer_1": 2,
                            "4_mu_integer_2": 1,
                            "4_eq_boolean": false
                        }
                    }
                }
            }
            """;

}