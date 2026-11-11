package com.pad.repitdoit.core.localdatabase

class testDb(
    private val database: RepItDoItDatabase
) {
    // Requiere haber cargado el catalogo mediante loadInitialDatabase().
    fun seed() {
        val userId = LOCAL_TEST_USER_ID
        val planId = "test-plan-001"

        val existingPlan = database.trainingPlanQueries
            .selectPlanById(planId, userId)
            .executeAsOneOrNull()

        if (existingPlan != null) return

        database.transaction {

            // 1. Usuario
            val user = database.userQueries
                .selectUserById(userId)
                .executeAsOneOrNull()

            if (user == null) {
                database.userQueries.insertUser(
                    id = userId,
                    username = "TestUser",
                    display_name = "Usuario de prueba",
                    last_synced_at = null
                )
            }

            // 2. Rutinas
            database.workoutQueries.insertWorkout(
                id = "test-workout-001",
                user_id = userId,
                name = "Push Day",
                description = "Entrenamiento de empuje",
                image_url = "training_muscles",
                estimated_time = 60L,
                created_at = 1000L,
                updated_at = 1000L
            )

            database.workoutQueries.insertWorkout(
                id = "test-workout-002",
                user_id = userId,
                name = "Cardio",
                description = "Entrenamiento cardiovascular",
                image_url = null,
                estimated_time = 30L,
                created_at = 1000L,
                updated_at = 1000L
            )

            // 3. Ejercicios del catalogo asociados a Push Day
            database.workoutExerciseQueries
                .insertWorkoutExercise(
                    id = "test-we-001",
                    workout_id = "test-workout-001",
                    exercise_id = "908d354a-8282-5726-9115-9f322536e428",
                    position = 0L,
                    target_weight = 20.0,
                    target_sets = 4L,
                    target_reps = 10L,
                    rest_seconds = 90L,
                    notes = null
                )

            database.workoutExerciseQueries
                .insertWorkoutExercise(
                    id = "test-we-002",
                    workout_id = "test-workout-001",
                    exercise_id = "05997e70-373b-5097-8be8-2037ea2b82f0",
                    position = 1L,
                    target_weight = 20.0,
                    target_sets = 3L,
                    target_reps = 12L,
                    rest_seconds = 60L,
                    notes = null
                )

            // 4. Plan de entrenamiento
            database.trainingPlanQueries.insertTrainingPlan(
                id = planId,
                user_id = userId,
                name = "Plan de prueba",
                created_at = 1000L,
                updated_at = 1000L
            )

            // Desactivar cualquier plan anterior
            database.trainingPlanQueries.deactivateUserPlans(
                updated_at = 1000L,
                user_id = userId
            )

            // Activar el plan de prueba
            database.trainingPlanQueries.activateTrainingPlan(
                updated_at = 1000L,
                id = planId,
                user_id = userId
            )

            // 5. Programar ambas rutinas
            // para todos los días de la semana
            for (day in 1L..7L) {

                database.workoutScheduleQueries
                    .insertWorkoutSchedule(
                        id = "test-push-$day",
                        user_id = userId,
                        training_plan_id = planId,
                        workout_id = "test-workout-001",
                        weekday = day,
                        position = 0L
                    )

                database.workoutScheduleQueries
                    .insertWorkoutSchedule(
                        id = "test-cardio-$day",
                        user_id = userId,
                        training_plan_id = planId,
                        workout_id = "test-workout-002",
                        weekday = day,
                        position = 1L
                    )
            }
        }
    }

    // Catalogo inicial: ../docs/RepItDoIt_catalogo_inicial.sql.
    // Conserva los registros existentes y permite repetir la carga sin duplicados.
    fun loadInitialDatabase() = database.transaction {
        seedEquipmentBatch0()
        seedExerciseBatch0()
        seedExerciseBatch1()
        seedExerciseBatch2()
        seedMuscleBatch0()
        seedMuscleExerciseBatch0()
        seedMuscleExerciseBatch1()
        seedMuscleExerciseBatch2()
        seedMuscleExerciseBatch3()
        seedMuscleExerciseBatch4()
        seedMuscleExerciseBatch5()
        seedMuscleExerciseBatch6()
        seedExerciseEquipmentBatch0()
        seedExerciseEquipmentBatch1()
        seedExerciseEquipmentBatch2()
        seedExerciseEquipmentBatch3()
        seedExerciseEquipmentBatch4()
    }

    private fun seedEquipment(id: String, name: String, description: String?, is_active: Long, updated_at: Long) {
        if (database.equipmentQueries.selectEquipmentById(id).executeAsOneOrNull() == null) {
            database.equipmentQueries.insertEquipment(id, name, description, is_active, updated_at)
        }
    }

    private fun seedEquipmentBatch0() {
        seedEquipment("82746ac5-02c6-5bf2-856b-f529a6e0d86a", "Peso corporal", "Ejercicios sin carga externa; pueden requerir además un soporte.", 1L, 1793275200000L)
        seedEquipment("1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c", "Mancuernas", "Una o dos mancuernas.", 1L, 1793275200000L)
        seedEquipment("79b1a0b9-c470-55a9-b7a9-7842077ec0e0", "Barra", "Barra de musculación.", 1L, 1793275200000L)
        seedEquipment("acae8a75-e01d-56d2-a988-09d6fd635f02", "Discos", "Discos de carga para barra o máquina.", 1L, 1793275200000L)
        seedEquipment("5ea74e33-ddbe-54b9-bc68-8f0706b60357", "Banco plano", "Banco de entrenamiento horizontal.", 1L, 1793275200000L)
        seedEquipment("d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb", "Banco regulable", "Banco con ajuste de inclinación.", 1L, 1793275200000L)
        seedEquipment("f06c9206-1ae5-5991-80d1-8aa34cbd5125", "Rack o soportes", "Soporte para colocar y asegurar una barra.", 1L, 1793275200000L)
        seedEquipment("ee524017-7e84-5822-a5e8-1dc1c67b3878", "Estación de poleas", "Máquina con polea regulable.", 1L, 1793275200000L)
        seedEquipment("5a12e785-1773-59d6-8d56-b5516274ca38", "Máquina de jalón", "Máquina de jalón al pecho.", 1L, 1793275200000L)
        seedEquipment("50281eee-973e-568b-864c-8f82b8cc59eb", "Máquina de remo sentado", "Máquina de remo con apoyo y resistencia guiada.", 1L, 1793275200000L)
        seedEquipment("ecd1ba48-fab0-53b9-8ff6-43a1425d791b", "Máquina de press de pecho", "Máquina guiada para empujes de pecho.", 1L, 1793275200000L)
        seedEquipment("1f1d4de6-b06a-52d9-9de7-6aac045df7e3", "Pec deck", "Máquina de aperturas o deltoides posterior.", 1L, 1793275200000L)
        seedEquipment("b8c028ce-da84-5f09-a9d3-c4bf133e53f4", "Máquina de press de hombros", "Máquina guiada para press vertical.", 1L, 1793275200000L)
        seedEquipment("e499d26f-34df-5fd6-b159-0fe51d4861fd", "Máquina Smith", "Barra guiada vertical o casi vertical.", 1L, 1793275200000L)
        seedEquipment("2a35407a-3354-556b-9c2d-b16a6dcb5bd2", "Barra de dominadas", "Barra fija o estructura para suspensión.", 1L, 1793275200000L)
        seedEquipment("f598eb88-83d8-5672-bdfa-0b80f6f1d498", "Paralelas", "Soporte de fondos.", 1L, 1793275200000L)
        seedEquipment("ddf5aef1-56bc-58a4-bf62-c0efbf5d6871", "Banco predicador", "Banco de curl con apoyo de brazos.", 1L, 1793275200000L)
        seedEquipment("dee340b0-82e5-58cb-bcfa-1ba830a75a06", "Prensa de piernas", "Máquina de prensa.", 1L, 1793275200000L)
        seedEquipment("12b39133-d575-5f02-9d31-2cf794792ead", "Extensión de piernas", "Máquina de extensión de rodilla.", 1L, 1793275200000L)
        seedEquipment("745341f9-46a5-5d4d-98bc-59472c97c71b", "Curl femoral", "Máquina de flexión de rodilla.", 1L, 1793275200000L)
        seedEquipment("ec5bfb1c-95e7-5828-b187-2bac37e1eb4c", "Máquina de abductores", "Máquina para abducción de cadera.", 1L, 1793275200000L)
        seedEquipment("f5935721-3eef-5943-b053-39c55152774c", "Máquina de aductores", "Máquina para aducción de cadera.", 1L, 1793275200000L)
        seedEquipment("9cf6a791-c486-5aea-8dd0-c6d9618eda7a", "Máquina de gemelos", "Máquina específica de elevación de talones.", 1L, 1793275200000L)
        seedEquipment("2bde466c-37f3-515c-8f13-dd363396c3dd", "Banco de hiperextensiones", "Banco romano de extensión de espalda.", 1L, 1793275200000L)
        seedEquipment("f5ae0440-8f58-592a-af73-bc8fce5834e8", "Kettlebell", "Pesa rusa.", 1L, 1793275200000L)
        seedEquipment("767cb4a8-03b9-576e-82bb-e40a83f16b4d", "Bandas elásticas", "Bandas de resistencia.", 1L, 1793275200000L)
        seedEquipment("a5a04939-f923-5184-9b6a-3881a9eaaaa3", "Esterilla", "Superficie acolchada para ejercicios en suelo.", 1L, 1793275200000L)
        seedEquipment("075aa15f-8c15-5977-9ffc-f2349a36f65b", "Soporte landmine", "Anclaje pivotante para un extremo de la barra.", 1L, 1793275200000L)
        seedEquipment("1f46bef4-fb3c-53d7-8154-f9b6dbb48a24", "Entrenador en suspensión", "Correas de entrenamiento en suspensión.", 1L, 1793275200000L)
        seedEquipment("28257dd6-9907-5571-ab10-3730ec817e17", "Máquina de hip thrust", "Máquina guiada de extensión de cadera.", 1L, 1793275200000L)
        seedEquipment("cd21de91-9310-581d-aeae-c20bca2bc629", "Cajón o step", "Plataforma estable para subidas.", 1L, 1793275200000L)
        seedEquipment("b98c3a1a-1972-5a55-be8f-d2ef5478e394", "Cuerda de polea", "Accesorio de agarre para polea.", 1L, 1793275200000L)
        seedEquipment("cdca829a-a389-5574-a173-4b5ba9f63bc5", "Agarre de polea", "Agarre simple o doble para polea.", 1L, 1793275200000L)
        seedEquipment("b00c5efa-4686-5d1c-87d4-da029115ca70", "Barra EZ", "Barra curva para ejercicios de brazos.", 1L, 1793275200000L)
        seedEquipment("21ba1ccc-c6d6-5c22-93fc-82b2b081fa01", "Rueda abdominal", "Rueda para ejercicios de core.", 1L, 1793275200000L)
    }

    private fun seedExercise(id: String, name: String, description: String?, image_url: String?, video_url: String?, is_active: Long, updated_at: Long) {
        if (database.exerciseQueries.selectExerciseById(id).executeAsOneOrNull() == null) {
            database.exerciseQueries.insertExercise(id, name, description, image_url, video_url, is_active, updated_at)
        }
    }

    private fun seedExerciseBatch0() {
        seedExercise("908d354a-8282-5726-9115-9f322536e428", "Press de banca con barra", "Empuje horizontal tumbado en banco plano con barra.", null, null, 1L, 1793275200000L)
        seedExercise("994fb7ee-33d7-556a-bf1f-c6f72906a062", "Press de banca con mancuernas", "Empuje horizontal con mancuernas en banco plano.", null, null, 1L, 1793275200000L)
        seedExercise("9fb31747-d710-5b7f-afb2-65b480204243", "Press inclinado con barra", "Press de pecho sobre banco inclinado con barra.", null, null, 1L, 1793275200000L)
        seedExercise("1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d", "Press inclinado con mancuernas", "Press de pecho sobre banco inclinado con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("68f94e8e-92fe-5f58-a364-93065c8af0bd", "Press declinado con barra", "Press de pecho en banco declinado, si el banco permite esta posición.", null, null, 1L, 1793275200000L)
        seedExercise("108d73b4-664c-5b93-bf07-87bbc27fded1", "Press de pecho en máquina", "Empuje de pecho sentado en máquina guiada.", null, null, 1L, 1793275200000L)
        seedExercise("a755ed86-eb89-55d3-8c75-cfd03d0b410e", "Press de banca en multipower", "Empuje horizontal con barra guiada en banco.", null, null, 1L, 1793275200000L)
        seedExercise("9b311165-4d37-5bc8-a310-b4c4e1e5d32d", "Aperturas con mancuernas", "Apertura y cierre controlado de brazos tumbado en banco.", null, null, 1L, 1793275200000L)
        seedExercise("f3fde6ec-d377-5281-b35a-2cfe25f59eef", "Aperturas inclinadas con mancuernas", "Aperturas sobre banco inclinado.", null, null, 1L, 1793275200000L)
        seedExercise("3062ffbe-1889-53ec-838a-4a5447c81c8d", "Cruces de poleas", "Aducción de brazos con resistencia de poleas.", null, null, 1L, 1793275200000L)
        seedExercise("5672beb1-ff80-5075-9482-fa03a9ae79da", "Aperturas en pec deck", "Aperturas de pecho en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("10987737-f9ae-5cb1-a543-f04a0ff2215e", "Flexiones", "Flexión y extensión de brazos en posición de plancha.", null, null, 1L, 1793275200000L)
        seedExercise("be247481-343d-58da-a32f-ac428582dce5", "Flexiones inclinadas", "Flexiones con las manos elevadas sobre banco estable.", null, null, 1L, 1793275200000L)
        seedExercise("41cc14fb-c298-529f-9137-fd0122febec2", "Flexiones diamante", "Flexiones con manos juntas, mayor demanda de tríceps.", null, null, 1L, 1793275200000L)
        seedExercise("da0f096b-308b-54f8-af57-280c25eb2dcd", "Fondos enfocados en pecho", "Fondos en paralelas con ligera inclinación del tronco.", null, null, 1L, 1793275200000L)
        seedExercise("347a1aed-eec2-5a14-a010-215e8f95f83f", "Dominadas pronas", "Tracción vertical suspendido con agarre prono.", null, null, 1L, 1793275200000L)
        seedExercise("a44317f6-1fd0-5d9c-9c0f-7e05acc037c4", "Dominadas supinas", "Tracción vertical con agarre supino.", null, null, 1L, 1793275200000L)
        seedExercise("707dbd39-c6f6-5a91-a432-d102c53495f2", "Dominadas neutras", "Tracción vertical con agarre neutro, si la barra lo permite.", null, null, 1L, 1793275200000L)
        seedExercise("2ed4071f-7f72-5fdc-9551-eb7cb896e015", "Jalón al pecho agarre ancho", "Tracción vertical hacia la parte superior del pecho.", null, null, 1L, 1793275200000L)
        seedExercise("0dd33efa-9e67-51b0-a977-63de771b947e", "Jalón al pecho agarre neutro", "Jalón vertical con agarre neutro.", null, null, 1L, 1793275200000L)
        seedExercise("cbe205fd-8784-5f1c-b577-af279ef584bb", "Jalón al pecho supino", "Jalón vertical con palmas orientadas hacia el cuerpo.", null, null, 1L, 1793275200000L)
        seedExercise("de7e0561-4597-5602-8e79-b34b16551b60", "Remo con barra", "Remo con tronco inclinado y barra libre.", null, null, 1L, 1793275200000L)
        seedExercise("8a44c5e1-1a29-5014-bcd8-2840042418b7", "Remo Pendlay", "Remo desde el suelo con tronco casi paralelo al suelo.", null, null, 1L, 1793275200000L)
        seedExercise("a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1", "Remo con mancuerna a una mano", "Remo unilateral con apoyo en banco.", null, null, 1L, 1793275200000L)
        seedExercise("a9ad4c7a-9a11-5815-9853-95adafb68c6b", "Remo inclinado con mancuernas", "Remo bilateral inclinado con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("42f9f70d-3032-5b92-91ed-8844b718cdf4", "Remo sentado en polea", "Remo horizontal sentado con cable.", null, null, 1L, 1793275200000L)
        seedExercise("fb8f58eb-6724-50b2-9363-6e38c9a4ca01", "Remo en máquina", "Tracción horizontal guiada en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("d95d8324-5d3f-5e2d-a32d-fa2557887982", "Remo con pecho apoyado", "Remo sobre banco inclinado con apoyo de pecho.", null, null, 1L, 1793275200000L)
        seedExercise("5eeb83ec-8fd4-5e24-9728-97d1bf164e3b", "Pullover con brazos rectos en polea", "Extensión de hombro de pie con brazos casi rectos.", null, null, 1L, 1793275200000L)
        seedExercise("b3eaafde-ced8-50ce-9542-5eb1e7ecbd58", "Pullover con mancuerna", "Movimiento de mancuerna en arco sobre el torso.", null, null, 1L, 1793275200000L)
        seedExercise("501062f2-a304-5d3e-a030-3d8195c75b08", "Face pull en polea", "Tracción hacia el rostro con codos elevados.", null, null, 1L, 1793275200000L)
        seedExercise("6b4bf492-f6af-5f39-9375-4ac7d0863a40", "Encogimientos con barra", "Elevación controlada de hombros con barra.", null, null, 1L, 1793275200000L)
        seedExercise("5d51c529-0422-53fb-b08b-c737027e1426", "Encogimientos con mancuernas", "Elevación controlada de hombros con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("05997e70-373b-5097-8be8-2037ea2b82f0", "Press militar con barra", "Press vertical de pie con barra.", null, null, 1L, 1793275200000L)
        seedExercise("853f5566-385a-5280-95e9-f768bea9d866", "Press de hombros sentado con mancuernas", "Press vertical sentado con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("ee11c194-45e2-5100-af83-500393829350", "Press Arnold", "Press de hombros con rotación de las mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("50af7963-19cf-535e-b88d-57ed0d12b316", "Press de hombros en máquina", "Press vertical guiado sentado.", null, null, 1L, 1793275200000L)
        seedExercise("6f74b34b-a36a-5dd7-a492-7381798847ca", "Press militar en multipower", "Press vertical con barra guiada.", null, null, 1L, 1793275200000L)
        seedExercise("318fe537-3564-5f7a-b242-ad53506247f8", "Elevaciones laterales con mancuernas", "Abducción controlada de hombro con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("8481e25d-54cf-5d8a-9fc9-880bde0d7847", "Elevaciones laterales en polea", "Abducción unilateral de hombro con cable.", null, null, 1L, 1793275200000L)
    }

    private fun seedExerciseBatch1() {
        seedExercise("5fd63060-604d-5369-9119-df1b59aa9a71", "Elevaciones frontales con mancuernas", "Elevación anterior de brazos con mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("ad35c535-6040-5273-bffa-6ec8bc3545da", "Pájaros con mancuernas", "Apertura de brazos con tronco inclinado.", null, null, 1L, 1793275200000L)
        seedExercise("30fe7001-05f1-5747-bc50-f25d077483f2", "Pájaros en pec deck inverso", "Aperturas inversas guiadas en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("a782442b-e605-525f-b2a7-581b2791121f", "Aperturas inversas en polea", "Aperturas posteriores de hombro mediante cable.", null, null, 1L, 1793275200000L)
        seedExercise("59cc95b7-a120-58a1-a2f5-e2951475f954", "Remo al mentón con mancuernas", "Tracción vertical corta con mancuernas y rango cómodo.", null, null, 1L, 1793275200000L)
        seedExercise("7298853d-3859-58bd-8f23-95078f371671", "Curl de bíceps con barra", "Flexión de codos de pie con barra.", null, null, 1L, 1793275200000L)
        seedExercise("f00740be-b152-5a8d-a55e-9e3dca8019be", "Curl de bíceps con barra EZ", "Flexión de codos de pie con barra curva.", null, null, 1L, 1793275200000L)
        seedExercise("cd325c04-2c2e-5449-892f-4f10e0389d64", "Curl alterno con mancuernas", "Flexión alterna de codos con supinación.", null, null, 1L, 1793275200000L)
        seedExercise("0e357f3b-43bf-5c10-a5d6-682aea0d7cb5", "Curl martillo", "Curl con agarre neutro.", null, null, 1L, 1793275200000L)
        seedExercise("85ce2930-b984-5ad9-a4a0-1a5dcd65ff5b", "Curl inclinado con mancuernas", "Curl sentado con respaldo inclinado.", null, null, 1L, 1793275200000L)
        seedExercise("2331302d-529c-5b04-aad0-3beca0a892e7", "Curl concentrado", "Curl unilateral con el brazo apoyado en el muslo.", null, null, 1L, 1793275200000L)
        seedExercise("acbe35b1-de7b-58b5-9ddf-155a01d98de1", "Curl predicador con barra EZ", "Curl con brazos apoyados en banco predicador.", null, null, 1L, 1793275200000L)
        seedExercise("77425758-2d20-5b95-afe2-8f667ad981a0", "Curl predicador con mancuerna", "Curl unilateral con apoyo en banco predicador.", null, null, 1L, 1793275200000L)
        seedExercise("28afaee1-db71-5ad8-8853-c1b38241bfcf", "Curl de bíceps en polea", "Flexión de codos con cable.", null, null, 1L, 1793275200000L)
        seedExercise("14a700bc-ccab-579f-ae06-fadea778b186", "Curl bayesiano en polea", "Curl unilateral con cable situado detrás del cuerpo.", null, null, 1L, 1793275200000L)
        seedExercise("a4a4d1af-7a83-5977-96b8-5a662f9c5d40", "Extensión de tríceps con cuerda", "Extensión de codos hacia abajo con cuerda.", null, null, 1L, 1793275200000L)
        seedExercise("b194b7cd-26c6-56f9-8bcf-07a9991f7c48", "Extensión de tríceps con barra en polea", "Extensión de codos en polea alta con barra de agarre.", null, null, 1L, 1793275200000L)
        seedExercise("9fccc47b-c8a7-5b2d-ac76-1fef5e9859b1", "Extensión de tríceps sobre cabeza con cuerda", "Extensión de codos con brazos elevados.", null, null, 1L, 1793275200000L)
        seedExercise("4b9332c5-1870-5aec-8c3d-ef432ca8e622", "Press francés tumbado con barra EZ", "Extensión de codos tumbado con barra EZ.", null, null, 1L, 1793275200000L)
        seedExercise("404c7144-34a9-5c97-92aa-4aff27a073f7", "Extensión de tríceps con mancuerna sobre cabeza", "Extensión de codos sujetando una mancuerna.", null, null, 1L, 1793275200000L)
        seedExercise("1201d468-c410-515a-bb12-e0923e7c4659", "Press de banca con agarre cerrado", "Press de banca con manos más juntas para priorizar tríceps.", null, null, 1L, 1793275200000L)
        seedExercise("6feaf01a-55a1-5c5c-9049-558b7a6eafc1", "Fondos en banco", "Fondos con manos apoyadas en banco; usar rango confortable.", null, null, 1L, 1793275200000L)
        seedExercise("9ce45737-bbe5-53e7-9cca-783655e85241", "Fondos para tríceps en paralelas", "Fondos con tronco más vertical.", null, null, 1L, 1793275200000L)
        seedExercise("778fba82-a707-534a-a2aa-e0a2f78594ef", "Patada de tríceps con mancuerna", "Extensión de codo con el tronco inclinado.", null, null, 1L, 1793275200000L)
        seedExercise("7fd3a042-f71f-519c-b360-3b7274b41b16", "Extensión unilateral de tríceps en polea", "Extensión de codo con cable a un brazo.", null, null, 1L, 1793275200000L)
        seedExercise("c90339d6-640d-5734-b102-e5afe9d11118", "Sentadilla trasera con barra", "Sentadilla con barra apoyada en espalda superior.", null, null, 1L, 1793275200000L)
        seedExercise("922cc73f-3d8a-5b90-8f8b-489e18eeba5b", "Sentadilla frontal con barra", "Sentadilla con barra apoyada en la parte frontal de hombros.", null, null, 1L, 1793275200000L)
        seedExercise("f0b77e93-6708-51ed-94ca-b496bd5c54b5", "Sentadilla goblet", "Sentadilla sosteniendo una mancuerna frente al pecho.", null, null, 1L, 1793275200000L)
        seedExercise("aa9e2059-4dfa-5d05-b2c9-7b5c33a5ae5e", "Sentadilla en multipower", "Sentadilla con barra guiada.", null, null, 1L, 1793275200000L)
        seedExercise("cda428ec-9108-5980-848e-8908a3163330", "Sentadilla con peso corporal", "Sentadilla sin carga externa.", null, null, 1L, 1793275200000L)
        seedExercise("a52b3544-192e-57dd-973f-d52b201179a2", "Prensa de piernas", "Empuje de plataforma de prensa con los pies.", null, null, 1L, 1793275200000L)
        seedExercise("4e233d96-eb4c-5e7f-8898-3c376c6f3d32", "Extensiones de cuádriceps", "Extensión de rodilla sentado en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("c1d8c32d-931a-581d-a84b-990bcbdf1b50", "Zancadas caminando con mancuernas", "Zancadas alternando piernas y avanzando.", null, null, 1L, 1793275200000L)
        seedExercise("69c89438-39b7-5f4c-8674-c40fc8158504", "Zancada hacia atrás con mancuernas", "Zancada dando un paso atrás.", null, null, 1L, 1793275200000L)
        seedExercise("7cb71d0f-35ad-536d-b24f-29c2063e4a9d", "Sentadilla búlgara con mancuernas", "Sentadilla unilateral con pie posterior elevado.", null, null, 1L, 1793275200000L)
        seedExercise("ad140972-5ace-538a-bfaf-fcdb0fc53726", "Subida a cajón con mancuernas", "Subida a plataforma estable sosteniendo mancuernas.", null, null, 1L, 1793275200000L)
        seedExercise("ac9d7754-9cb3-54d9-a5e8-32b3438554d5", "Sentadilla isométrica en pared", "Mantener posición de sentadilla contra pared.", null, null, 1L, 1793275200000L)
        seedExercise("d6873a49-4db6-554a-9a0f-a690286de4a6", "Peso muerto convencional", "Levantamiento de barra desde el suelo con bisagra de cadera y rodillas.", null, null, 1L, 1793275200000L)
        seedExercise("763d1380-633b-5876-b587-b668928da4f4", "Peso muerto rumano con barra", "Bisagra de cadera con barra y rodillas ligeramente flexionadas.", null, null, 1L, 1793275200000L)
        seedExercise("9ce08743-09da-50af-a8f7-2212b2caaf67", "Peso muerto rumano con mancuernas", "Bisagra de cadera sosteniendo mancuernas.", null, null, 1L, 1793275200000L)
    }

    private fun seedExerciseBatch2() {
        seedExercise("7f3f9270-5a28-513e-99c0-0dd6eb2c5adc", "Peso muerto sumo", "Peso muerto con postura amplia.", null, null, 1L, 1793275200000L)
        seedExercise("d6dbd523-c081-57fb-868a-492cc503284f", "Curl femoral sentado", "Flexión de rodilla sentado en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("fa3454ba-cd22-5186-ab38-4e8b91dc27d7", "Curl femoral tumbado", "Flexión de rodilla tumbado en máquina compatible.", null, null, 1L, 1793275200000L)
        seedExercise("2e626b00-1bd1-5507-b52c-1294997c40c1", "Peso muerto rumano unilateral con mancuerna", "Bisagra de cadera a una pierna con mancuerna.", null, null, 1L, 1793275200000L)
        seedExercise("41ca39e6-e04d-5a78-a801-8c079cb36290", "Hip thrust con barra", "Extensión de cadera con espalda apoyada en banco y barra sobre pelvis.", null, null, 1L, 1793275200000L)
        seedExercise("0e4a214c-c322-5d73-9415-e4dacbc2bde9", "Hip thrust en máquina", "Extensión de cadera en máquina.", null, null, 1L, 1793275200000L)
        seedExercise("26dc4363-acfa-5b24-b9d4-1dffe14491e0", "Puente de glúteos", "Extensión de cadera tumbado boca arriba.", null, null, 1L, 1793275200000L)
        seedExercise("ae9d1a74-773b-5fc7-9119-a3d1a901b8c8", "Patada de glúteos en polea", "Extensión de cadera unilateral con polea y tobillera integrada o compatible.", null, null, 1L, 1793275200000L)
        seedExercise("39d5879c-a3ac-5790-bbc6-1aa3f4a7d316", "Abducción de cadera en máquina", "Separación de muslos contra resistencia guiada.", null, null, 1L, 1793275200000L)
        seedExercise("fa858e71-5efa-5400-8c15-02ec4789fda6", "Aducción de cadera en máquina", "Acercamiento de muslos contra resistencia guiada.", null, null, 1L, 1793275200000L)
        seedExercise("27f8d5df-626a-580e-965a-2e237bef14aa", "Elevaciones de gemelos de pie con mancuernas", "Elevación y descenso de talones de pie.", null, null, 1L, 1793275200000L)
        seedExercise("4766745c-bf63-549c-97e6-64579959b133", "Elevaciones de gemelos sentado en máquina", "Elevación y descenso de talones con rodillas flexionadas.", null, null, 1L, 1793275200000L)
        seedExercise("2ad8248a-16d3-5595-83df-7fb68e7493d7", "Plancha frontal", "Mantener una posición de plancha sobre antebrazos.", null, null, 1L, 1793275200000L)
        seedExercise("99ba4f38-55e6-53c5-b44a-ca93aadb4dcc", "Plancha lateral", "Mantener apoyo lateral sobre antebrazo.", null, null, 1L, 1793275200000L)
        seedExercise("8a312b00-55d6-5ae7-9dac-b024d56faa7b", "Crunch abdominal", "Flexión controlada de tronco tumbado.", null, null, 1L, 1793275200000L)
        seedExercise("b16b3c98-3a8b-5280-a28e-e98dae1f0e6d", "Crunch inverso", "Retroversión de pelvis y elevación controlada de caderas.", null, null, 1L, 1793275200000L)
        seedExercise("8ed001dc-3a6e-5312-b6b5-f9b3f972e0e4", "Elevaciones de piernas tumbado", "Elevación de piernas con control lumbar.", null, null, 1L, 1793275200000L)
        seedExercise("e32e0151-a9af-5627-92ac-cc0a0ef011a8", "Elevaciones de rodillas colgado", "Elevación de rodillas suspendido de barra.", null, null, 1L, 1793275200000L)
        seedExercise("8476a337-d4be-53fc-b73a-90443486fa61", "Elevaciones de piernas colgado", "Elevación de piernas suspendido de barra.", null, null, 1L, 1793275200000L)
        seedExercise("f5cdf847-6ec2-5ec5-81b6-64e22893075c", "Crunch en polea", "Flexión de tronco en rodillas contra cable.", null, null, 1L, 1793275200000L)
        seedExercise("60857cb5-95f1-5e96-9f7c-a912c2aecbb7", "Giros rusos", "Rotación de tronco sentado con pies apoyados o elevados.", null, null, 1L, 1793275200000L)
        seedExercise("2dbc1411-fce5-54ca-a55c-c7e275960ee8", "Rueda abdominal", "Desplazamiento controlado hacia delante con rueda abdominal.", null, null, 1L, 1793275200000L)
        seedExercise("46019347-494f-5e59-ae64-37cab695af7b", "Extensiones lumbares en banco", "Extensión de tronco en banco romano.", null, null, 1L, 1793275200000L)
        seedExercise("5814c728-823d-5553-a536-4184b8643403", "Curl de muñeca con mancuernas", "Flexión de muñeca con antebrazo apoyado.", null, null, 1L, 1793275200000L)
        seedExercise("322e5f12-5e94-5f44-a3ba-ba5ebac6e140", "Extensión de muñeca con mancuernas", "Extensión de muñeca con antebrazo apoyado.", null, null, 1L, 1793275200000L)
        seedExercise("f59ca334-ebc3-5d1a-ab8a-894d30759186", "Paseo del granjero con mancuernas", "Caminar sosteniendo dos mancuernas con postura estable.", null, null, 1L, 1793275200000L)
        seedExercise("bf2ecc6d-891a-5139-b2b2-785889c44854", "Swing con kettlebell", "Balanceo con bisagra de cadera y extensión explosiva.", null, null, 1L, 1793275200000L)
        seedExercise("6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf", "Remo con barra en landmine", "Remo con extremo de barra anclado.", null, null, 1L, 1793275200000L)
        seedExercise("da2037c4-1eb1-566f-9ce7-ddfb020fc49b", "Remo en suspensión", "Remo inclinado usando correas en suspensión.", null, null, 1L, 1793275200000L)
        seedExercise("32416061-25a8-525e-b338-d5b12e43545c", "Aperturas con banda elástica", "Separación de manos con banda a la altura del pecho.", null, null, 1L, 1793275200000L)
        seedExercise("d1e70a83-9e4d-54bd-b5c9-7e89cc1e0b04", "Pasos laterales con banda", "Desplazamiento lateral con banda de resistencia.", null, null, 1L, 1793275200000L)
        seedExercise("ca53839c-cdfd-5f5a-82c1-1b8d92b71617", "Elevaciones de gemelos con banda", "Elevación de talones con resistencia de banda bien anclada.", null, null, 1L, 1793275200000L)
    }

    private fun seedMuscle(id: String, name: String, image_url: String?, muscle_group: String?) {
        if (database.muscleQueries.selectMuscleById(id).executeAsOneOrNull() == null) {
            database.muscleQueries.insertMuscle(id, name, image_url, muscle_group)
        }
    }

    private fun seedMuscleBatch0() {
        seedMuscle("c84c1112-8e98-5d67-9925-3825858a304a", "Pectoral", null, "Tren superior")
        seedMuscle("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "Dorsal ancho", null, "Espalda")
        seedMuscle("10701b87-97e5-5a3f-8e66-1af2256026bf", "Romboides y espalda media", null, "Espalda")
        seedMuscle("2ab9662c-3646-52e7-b78b-e5c363d4a708", "Trapecio", null, "Espalda")
        seedMuscle("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "Deltoides anterior", null, "Hombros")
        seedMuscle("90f04b8c-963b-5177-b163-b52d555133fa", "Deltoides lateral", null, "Hombros")
        seedMuscle("98f49f69-fa8d-512d-bacd-0c729806bab0", "Deltoides posterior", null, "Hombros")
        seedMuscle("529e8374-e008-5a66-b074-b76bcf77b538", "Bíceps", null, "Brazos")
        seedMuscle("26c05d03-200f-5460-9bac-e89d83fa1bb2", "Tríceps", null, "Brazos")
        seedMuscle("7c652126-cdb2-51a6-b95d-4225b21028d6", "Antebrazos", null, "Brazos")
        seedMuscle("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "Recto abdominal", null, "Core")
        seedMuscle("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "Oblicuos", null, "Core")
        seedMuscle("f0b035d2-87a6-5829-9993-3e45a9f146ca", "Erectores espinales", null, "Core")
        seedMuscle("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "Cuádriceps", null, "Piernas")
        seedMuscle("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "Isquiotibiales", null, "Piernas")
        seedMuscle("1ee92ffe-33b0-580e-9bcc-9da042742a58", "Glúteos", null, "Piernas")
        seedMuscle("801e454b-7c85-5513-beb1-dd8553234ff5", "Aductores", null, "Piernas")
        seedMuscle("95d53e06-ac76-5c7d-9331-5dbf027d52de", "Abductores de cadera", null, "Piernas")
        seedMuscle("0282ca01-a60b-5d35-8d4a-32248e6c995b", "Gemelos y sóleo", null, "Piernas")
    }

    private fun seedMuscleExercise(muscle_id: String, exercise_id: String) {
        if (database.muscleExerciseQueries.selectMuscleExercise(muscle_id, exercise_id).executeAsOneOrNull() == null) {
            database.muscleExerciseQueries.insertMuscleExercise(muscle_id, exercise_id)
        }
    }

    private fun seedMuscleExerciseBatch0() {
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "908d354a-8282-5726-9115-9f322536e428")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "908d354a-8282-5726-9115-9f322536e428")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "908d354a-8282-5726-9115-9f322536e428")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "994fb7ee-33d7-556a-bf1f-c6f72906a062")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "994fb7ee-33d7-556a-bf1f-c6f72906a062")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "994fb7ee-33d7-556a-bf1f-c6f72906a062")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "9fb31747-d710-5b7f-afb2-65b480204243")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "9fb31747-d710-5b7f-afb2-65b480204243")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "9fb31747-d710-5b7f-afb2-65b480204243")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "68f94e8e-92fe-5f58-a364-93065c8af0bd")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "68f94e8e-92fe-5f58-a364-93065c8af0bd")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "68f94e8e-92fe-5f58-a364-93065c8af0bd")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "108d73b4-664c-5b93-bf07-87bbc27fded1")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "108d73b4-664c-5b93-bf07-87bbc27fded1")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "108d73b4-664c-5b93-bf07-87bbc27fded1")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "a755ed86-eb89-55d3-8c75-cfd03d0b410e")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "a755ed86-eb89-55d3-8c75-cfd03d0b410e")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "a755ed86-eb89-55d3-8c75-cfd03d0b410e")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "9b311165-4d37-5bc8-a310-b4c4e1e5d32d")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "9b311165-4d37-5bc8-a310-b4c4e1e5d32d")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "f3fde6ec-d377-5281-b35a-2cfe25f59eef")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "f3fde6ec-d377-5281-b35a-2cfe25f59eef")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "3062ffbe-1889-53ec-838a-4a5447c81c8d")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "3062ffbe-1889-53ec-838a-4a5447c81c8d")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "5672beb1-ff80-5075-9482-fa03a9ae79da")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "5672beb1-ff80-5075-9482-fa03a9ae79da")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "10987737-f9ae-5cb1-a543-f04a0ff2215e")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "10987737-f9ae-5cb1-a543-f04a0ff2215e")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "10987737-f9ae-5cb1-a543-f04a0ff2215e")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "be247481-343d-58da-a32f-ac428582dce5")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "be247481-343d-58da-a32f-ac428582dce5")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "be247481-343d-58da-a32f-ac428582dce5")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "41cc14fb-c298-529f-9137-fd0122febec2")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "41cc14fb-c298-529f-9137-fd0122febec2")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "41cc14fb-c298-529f-9137-fd0122febec2")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "da0f096b-308b-54f8-af57-280c25eb2dcd")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "da0f096b-308b-54f8-af57-280c25eb2dcd")
    }

    private fun seedMuscleExerciseBatch1() {
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "da0f096b-308b-54f8-af57-280c25eb2dcd")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "347a1aed-eec2-5a14-a010-215e8f95f83f")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "347a1aed-eec2-5a14-a010-215e8f95f83f")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "347a1aed-eec2-5a14-a010-215e8f95f83f")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "a44317f6-1fd0-5d9c-9c0f-7e05acc037c4")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "a44317f6-1fd0-5d9c-9c0f-7e05acc037c4")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "a44317f6-1fd0-5d9c-9c0f-7e05acc037c4")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "707dbd39-c6f6-5a91-a432-d102c53495f2")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "707dbd39-c6f6-5a91-a432-d102c53495f2")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "707dbd39-c6f6-5a91-a432-d102c53495f2")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "2ed4071f-7f72-5fdc-9551-eb7cb896e015")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "2ed4071f-7f72-5fdc-9551-eb7cb896e015")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "2ed4071f-7f72-5fdc-9551-eb7cb896e015")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "0dd33efa-9e67-51b0-a977-63de771b947e")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "0dd33efa-9e67-51b0-a977-63de771b947e")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "0dd33efa-9e67-51b0-a977-63de771b947e")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "cbe205fd-8784-5f1c-b577-af279ef584bb")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "cbe205fd-8784-5f1c-b577-af279ef584bb")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "de7e0561-4597-5602-8e79-b34b16551b60")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "de7e0561-4597-5602-8e79-b34b16551b60")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "de7e0561-4597-5602-8e79-b34b16551b60")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "de7e0561-4597-5602-8e79-b34b16551b60")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "de7e0561-4597-5602-8e79-b34b16551b60")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "8a44c5e1-1a29-5014-bcd8-2840042418b7")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "8a44c5e1-1a29-5014-bcd8-2840042418b7")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "8a44c5e1-1a29-5014-bcd8-2840042418b7")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "8a44c5e1-1a29-5014-bcd8-2840042418b7")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "8a44c5e1-1a29-5014-bcd8-2840042418b7")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "a9ad4c7a-9a11-5815-9853-95adafb68c6b")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "a9ad4c7a-9a11-5815-9853-95adafb68c6b")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "a9ad4c7a-9a11-5815-9853-95adafb68c6b")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "a9ad4c7a-9a11-5815-9853-95adafb68c6b")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "a9ad4c7a-9a11-5815-9853-95adafb68c6b")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "42f9f70d-3032-5b92-91ed-8844b718cdf4")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "42f9f70d-3032-5b92-91ed-8844b718cdf4")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "42f9f70d-3032-5b92-91ed-8844b718cdf4")
    }

    private fun seedMuscleExerciseBatch2() {
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "42f9f70d-3032-5b92-91ed-8844b718cdf4")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "fb8f58eb-6724-50b2-9363-6e38c9a4ca01")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "fb8f58eb-6724-50b2-9363-6e38c9a4ca01")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "fb8f58eb-6724-50b2-9363-6e38c9a4ca01")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "d95d8324-5d3f-5e2d-a32d-fa2557887982")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "d95d8324-5d3f-5e2d-a32d-fa2557887982")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "d95d8324-5d3f-5e2d-a32d-fa2557887982")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "d95d8324-5d3f-5e2d-a32d-fa2557887982")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "5eeb83ec-8fd4-5e24-9728-97d1bf164e3b")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "5eeb83ec-8fd4-5e24-9728-97d1bf164e3b")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "b3eaafde-ced8-50ce-9542-5eb1e7ecbd58")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "b3eaafde-ced8-50ce-9542-5eb1e7ecbd58")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "b3eaafde-ced8-50ce-9542-5eb1e7ecbd58")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "501062f2-a304-5d3e-a030-3d8195c75b08")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "501062f2-a304-5d3e-a030-3d8195c75b08")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "501062f2-a304-5d3e-a030-3d8195c75b08")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "6b4bf492-f6af-5f39-9375-4ac7d0863a40")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "5d51c529-0422-53fb-b08b-c737027e1426")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "05997e70-373b-5097-8be8-2037ea2b82f0")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "05997e70-373b-5097-8be8-2037ea2b82f0")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "05997e70-373b-5097-8be8-2037ea2b82f0")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "853f5566-385a-5280-95e9-f768bea9d866")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "853f5566-385a-5280-95e9-f768bea9d866")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "853f5566-385a-5280-95e9-f768bea9d866")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "ee11c194-45e2-5100-af83-500393829350")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "ee11c194-45e2-5100-af83-500393829350")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "ee11c194-45e2-5100-af83-500393829350")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "50af7963-19cf-535e-b88d-57ed0d12b316")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "50af7963-19cf-535e-b88d-57ed0d12b316")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "50af7963-19cf-535e-b88d-57ed0d12b316")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "6f74b34b-a36a-5dd7-a492-7381798847ca")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "6f74b34b-a36a-5dd7-a492-7381798847ca")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "6f74b34b-a36a-5dd7-a492-7381798847ca")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "318fe537-3564-5f7a-b242-ad53506247f8")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "318fe537-3564-5f7a-b242-ad53506247f8")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "8481e25d-54cf-5d8a-9fc9-880bde0d7847")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "5fd63060-604d-5369-9119-df1b59aa9a71")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "ad35c535-6040-5273-bffa-6ec8bc3545da")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "ad35c535-6040-5273-bffa-6ec8bc3545da")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "30fe7001-05f1-5747-bc50-f25d077483f2")
    }

    private fun seedMuscleExerciseBatch3() {
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "30fe7001-05f1-5747-bc50-f25d077483f2")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "30fe7001-05f1-5747-bc50-f25d077483f2")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "a782442b-e605-525f-b2a7-581b2791121f")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "a782442b-e605-525f-b2a7-581b2791121f")
        seedMuscleExercise("90f04b8c-963b-5177-b163-b52d555133fa", "59cc95b7-a120-58a1-a2f5-e2951475f954")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "59cc95b7-a120-58a1-a2f5-e2951475f954")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "7298853d-3859-58bd-8f23-95078f371671")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "7298853d-3859-58bd-8f23-95078f371671")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "f00740be-b152-5a8d-a55e-9e3dca8019be")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "f00740be-b152-5a8d-a55e-9e3dca8019be")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "cd325c04-2c2e-5449-892f-4f10e0389d64")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "cd325c04-2c2e-5449-892f-4f10e0389d64")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "0e357f3b-43bf-5c10-a5d6-682aea0d7cb5")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "0e357f3b-43bf-5c10-a5d6-682aea0d7cb5")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "85ce2930-b984-5ad9-a4a0-1a5dcd65ff5b")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "2331302d-529c-5b04-aad0-3beca0a892e7")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "acbe35b1-de7b-58b5-9ddf-155a01d98de1")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "77425758-2d20-5b95-afe2-8f667ad981a0")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "28afaee1-db71-5ad8-8853-c1b38241bfcf")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "28afaee1-db71-5ad8-8853-c1b38241bfcf")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "14a700bc-ccab-579f-ae06-fadea778b186")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "a4a4d1af-7a83-5977-96b8-5a662f9c5d40")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "b194b7cd-26c6-56f9-8bcf-07a9991f7c48")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "9fccc47b-c8a7-5b2d-ac76-1fef5e9859b1")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "4b9332c5-1870-5aec-8c3d-ef432ca8e622")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "404c7144-34a9-5c97-92aa-4aff27a073f7")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "1201d468-c410-515a-bb12-e0923e7c4659")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "1201d468-c410-515a-bb12-e0923e7c4659")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "1201d468-c410-515a-bb12-e0923e7c4659")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "6feaf01a-55a1-5c5c-9049-558b7a6eafc1")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "6feaf01a-55a1-5c5c-9049-558b7a6eafc1")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "6feaf01a-55a1-5c5c-9049-558b7a6eafc1")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "9ce45737-bbe5-53e7-9cca-783655e85241")
        seedMuscleExercise("c84c1112-8e98-5d67-9925-3825858a304a", "9ce45737-bbe5-53e7-9cca-783655e85241")
        seedMuscleExercise("cd6a70b2-f393-54ee-9176-88c0fcb1501f", "9ce45737-bbe5-53e7-9cca-783655e85241")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "778fba82-a707-534a-a2aa-e0a2f78594ef")
        seedMuscleExercise("26c05d03-200f-5460-9bac-e89d83fa1bb2", "7fd3a042-f71f-519c-b360-3b7274b41b16")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "c90339d6-640d-5734-b102-e5afe9d11118")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "c90339d6-640d-5734-b102-e5afe9d11118")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "c90339d6-640d-5734-b102-e5afe9d11118")
    }

    private fun seedMuscleExerciseBatch4() {
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "c90339d6-640d-5734-b102-e5afe9d11118")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "922cc73f-3d8a-5b90-8f8b-489e18eeba5b")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "922cc73f-3d8a-5b90-8f8b-489e18eeba5b")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "922cc73f-3d8a-5b90-8f8b-489e18eeba5b")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "f0b77e93-6708-51ed-94ca-b496bd5c54b5")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "f0b77e93-6708-51ed-94ca-b496bd5c54b5")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "f0b77e93-6708-51ed-94ca-b496bd5c54b5")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "aa9e2059-4dfa-5d05-b2c9-7b5c33a5ae5e")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "aa9e2059-4dfa-5d05-b2c9-7b5c33a5ae5e")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "aa9e2059-4dfa-5d05-b2c9-7b5c33a5ae5e")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "cda428ec-9108-5980-848e-8908a3163330")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "cda428ec-9108-5980-848e-8908a3163330")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "cda428ec-9108-5980-848e-8908a3163330")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "a52b3544-192e-57dd-973f-d52b201179a2")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "a52b3544-192e-57dd-973f-d52b201179a2")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "a52b3544-192e-57dd-973f-d52b201179a2")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "4e233d96-eb4c-5e7f-8898-3c376c6f3d32")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "c1d8c32d-931a-581d-a84b-990bcbdf1b50")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "c1d8c32d-931a-581d-a84b-990bcbdf1b50")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "c1d8c32d-931a-581d-a84b-990bcbdf1b50")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "69c89438-39b7-5f4c-8674-c40fc8158504")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "69c89438-39b7-5f4c-8674-c40fc8158504")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "7cb71d0f-35ad-536d-b24f-29c2063e4a9d")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "7cb71d0f-35ad-536d-b24f-29c2063e4a9d")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "ad140972-5ace-538a-bfaf-fcdb0fc53726")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "ad140972-5ace-538a-bfaf-fcdb0fc53726")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "ac9d7754-9cb3-54d9-a5e8-32b3438554d5")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "ac9d7754-9cb3-54d9-a5e8-32b3438554d5")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "d6873a49-4db6-554a-9a0f-a690286de4a6")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "d6873a49-4db6-554a-9a0f-a690286de4a6")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "d6873a49-4db6-554a-9a0f-a690286de4a6")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "d6873a49-4db6-554a-9a0f-a690286de4a6")
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "d6873a49-4db6-554a-9a0f-a690286de4a6")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "763d1380-633b-5876-b587-b668928da4f4")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "763d1380-633b-5876-b587-b668928da4f4")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "763d1380-633b-5876-b587-b668928da4f4")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "9ce08743-09da-50af-a8f7-2212b2caaf67")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "9ce08743-09da-50af-a8f7-2212b2caaf67")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "9ce08743-09da-50af-a8f7-2212b2caaf67")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "7f3f9270-5a28-513e-99c0-0dd6eb2c5adc")
    }

    private fun seedMuscleExerciseBatch5() {
        seedMuscleExercise("4a13cd8b-cc50-5877-8c11-6de0c3ba011d", "7f3f9270-5a28-513e-99c0-0dd6eb2c5adc")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "7f3f9270-5a28-513e-99c0-0dd6eb2c5adc")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "7f3f9270-5a28-513e-99c0-0dd6eb2c5adc")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "7f3f9270-5a28-513e-99c0-0dd6eb2c5adc")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "d6dbd523-c081-57fb-868a-492cc503284f")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "fa3454ba-cd22-5186-ab38-4e8b91dc27d7")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "2e626b00-1bd1-5507-b52c-1294997c40c1")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "2e626b00-1bd1-5507-b52c-1294997c40c1")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "2e626b00-1bd1-5507-b52c-1294997c40c1")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "41ca39e6-e04d-5a78-a801-8c079cb36290")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "41ca39e6-e04d-5a78-a801-8c079cb36290")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "0e4a214c-c322-5d73-9415-e4dacbc2bde9")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "0e4a214c-c322-5d73-9415-e4dacbc2bde9")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "26dc4363-acfa-5b24-b9d4-1dffe14491e0")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "26dc4363-acfa-5b24-b9d4-1dffe14491e0")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "ae9d1a74-773b-5fc7-9119-a3d1a901b8c8")
        seedMuscleExercise("95d53e06-ac76-5c7d-9331-5dbf027d52de", "39d5879c-a3ac-5790-bbc6-1aa3f4a7d316")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "39d5879c-a3ac-5790-bbc6-1aa3f4a7d316")
        seedMuscleExercise("801e454b-7c85-5513-beb1-dd8553234ff5", "fa858e71-5efa-5400-8c15-02ec4789fda6")
        seedMuscleExercise("0282ca01-a60b-5d35-8d4a-32248e6c995b", "27f8d5df-626a-580e-965a-2e237bef14aa")
        seedMuscleExercise("0282ca01-a60b-5d35-8d4a-32248e6c995b", "4766745c-bf63-549c-97e6-64579959b133")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "2ad8248a-16d3-5595-83df-7fb68e7493d7")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "2ad8248a-16d3-5595-83df-7fb68e7493d7")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "99ba4f38-55e6-53c5-b44a-ca93aadb4dcc")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "99ba4f38-55e6-53c5-b44a-ca93aadb4dcc")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "8a312b00-55d6-5ae7-9dac-b024d56faa7b")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "b16b3c98-3a8b-5280-a28e-e98dae1f0e6d")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "8ed001dc-3a6e-5312-b6b5-f9b3f972e0e4")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "e32e0151-a9af-5627-92ac-cc0a0ef011a8")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "e32e0151-a9af-5627-92ac-cc0a0ef011a8")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "8476a337-d4be-53fc-b73a-90443486fa61")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "8476a337-d4be-53fc-b73a-90443486fa61")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "f5cdf847-6ec2-5ec5-81b6-64e22893075c")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "60857cb5-95f1-5e96-9f7c-a912c2aecbb7")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "60857cb5-95f1-5e96-9f7c-a912c2aecbb7")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "2dbc1411-fce5-54ca-a55c-c7e275960ee8")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "2dbc1411-fce5-54ca-a55c-c7e275960ee8")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "46019347-494f-5e59-ae64-37cab695af7b")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "46019347-494f-5e59-ae64-37cab695af7b")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "46019347-494f-5e59-ae64-37cab695af7b")
    }

    private fun seedMuscleExerciseBatch6() {
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "5814c728-823d-5553-a536-4184b8643403")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "322e5f12-5e94-5f44-a3ba-ba5ebac6e140")
        seedMuscleExercise("7c652126-cdb2-51a6-b95d-4225b21028d6", "f59ca334-ebc3-5d1a-ab8a-894d30759186")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "f59ca334-ebc3-5d1a-ab8a-894d30759186")
        seedMuscleExercise("ce2b0b84-2b5b-55d6-ae03-7a2e6f240be9", "f59ca334-ebc3-5d1a-ab8a-894d30759186")
        seedMuscleExercise("03ebe6b0-debc-5568-b1e4-1ba0354bdad8", "f59ca334-ebc3-5d1a-ab8a-894d30759186")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "bf2ecc6d-891a-5139-b2b2-785889c44854")
        seedMuscleExercise("59337eb2-2ae6-5991-8b8c-f2abe6a73fad", "bf2ecc6d-891a-5139-b2b2-785889c44854")
        seedMuscleExercise("f0b035d2-87a6-5829-9993-3e45a9f146ca", "bf2ecc6d-891a-5139-b2b2-785889c44854")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf")
        seedMuscleExercise("78050e56-ef6a-5cd7-8fb1-09835b3040c5", "da2037c4-1eb1-566f-9ce7-ddfb020fc49b")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "da2037c4-1eb1-566f-9ce7-ddfb020fc49b")
        seedMuscleExercise("529e8374-e008-5a66-b074-b76bcf77b538", "da2037c4-1eb1-566f-9ce7-ddfb020fc49b")
        seedMuscleExercise("98f49f69-fa8d-512d-bacd-0c729806bab0", "32416061-25a8-525e-b338-d5b12e43545c")
        seedMuscleExercise("10701b87-97e5-5a3f-8e66-1af2256026bf", "32416061-25a8-525e-b338-d5b12e43545c")
        seedMuscleExercise("2ab9662c-3646-52e7-b78b-e5c363d4a708", "32416061-25a8-525e-b338-d5b12e43545c")
        seedMuscleExercise("95d53e06-ac76-5c7d-9331-5dbf027d52de", "d1e70a83-9e4d-54bd-b5c9-7e89cc1e0b04")
        seedMuscleExercise("1ee92ffe-33b0-580e-9bcc-9da042742a58", "d1e70a83-9e4d-54bd-b5c9-7e89cc1e0b04")
        seedMuscleExercise("0282ca01-a60b-5d35-8d4a-32248e6c995b", "ca53839c-cdfd-5f5a-82c1-1b8d92b71617")
    }

    private fun seedExerciseEquipment(exercise_id: String, equipment_id: String) {
        if (database.exerciseEquipmentQueries.selectExerciseEquipment(exercise_id, equipment_id).executeAsOneOrNull() == null) {
            database.exerciseEquipmentQueries.insertExerciseEquipment(exercise_id, equipment_id)
        }
    }

    private fun seedExerciseEquipmentBatch0() {
        seedExerciseEquipment("908d354a-8282-5726-9115-9f322536e428", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("908d354a-8282-5726-9115-9f322536e428", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("908d354a-8282-5726-9115-9f322536e428", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("908d354a-8282-5726-9115-9f322536e428", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("994fb7ee-33d7-556a-bf1f-c6f72906a062", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("994fb7ee-33d7-556a-bf1f-c6f72906a062", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("9fb31747-d710-5b7f-afb2-65b480204243", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("9fb31747-d710-5b7f-afb2-65b480204243", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("9fb31747-d710-5b7f-afb2-65b480204243", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("9fb31747-d710-5b7f-afb2-65b480204243", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("1c6a7252-c296-55aa-9f2e-a2d1b5e28c9d", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("68f94e8e-92fe-5f58-a364-93065c8af0bd", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("68f94e8e-92fe-5f58-a364-93065c8af0bd", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("68f94e8e-92fe-5f58-a364-93065c8af0bd", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("68f94e8e-92fe-5f58-a364-93065c8af0bd", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("108d73b4-664c-5b93-bf07-87bbc27fded1", "ecd1ba48-fab0-53b9-8ff6-43a1425d791b")
        seedExerciseEquipment("a755ed86-eb89-55d3-8c75-cfd03d0b410e", "e499d26f-34df-5fd6-b159-0fe51d4861fd")
        seedExerciseEquipment("a755ed86-eb89-55d3-8c75-cfd03d0b410e", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("9b311165-4d37-5bc8-a310-b4c4e1e5d32d", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("9b311165-4d37-5bc8-a310-b4c4e1e5d32d", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("f3fde6ec-d377-5281-b35a-2cfe25f59eef", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("f3fde6ec-d377-5281-b35a-2cfe25f59eef", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("3062ffbe-1889-53ec-838a-4a5447c81c8d", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("3062ffbe-1889-53ec-838a-4a5447c81c8d", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("5672beb1-ff80-5075-9482-fa03a9ae79da", "1f1d4de6-b06a-52d9-9de7-6aac045df7e3")
        seedExerciseEquipment("10987737-f9ae-5cb1-a543-f04a0ff2215e", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("be247481-343d-58da-a32f-ac428582dce5", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("be247481-343d-58da-a32f-ac428582dce5", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("41cc14fb-c298-529f-9137-fd0122febec2", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("da0f096b-308b-54f8-af57-280c25eb2dcd", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("da0f096b-308b-54f8-af57-280c25eb2dcd", "f598eb88-83d8-5672-bdfa-0b80f6f1d498")
        seedExerciseEquipment("347a1aed-eec2-5a14-a010-215e8f95f83f", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("347a1aed-eec2-5a14-a010-215e8f95f83f", "2a35407a-3354-556b-9c2d-b16a6dcb5bd2")
        seedExerciseEquipment("a44317f6-1fd0-5d9c-9c0f-7e05acc037c4", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("a44317f6-1fd0-5d9c-9c0f-7e05acc037c4", "2a35407a-3354-556b-9c2d-b16a6dcb5bd2")
        seedExerciseEquipment("707dbd39-c6f6-5a91-a432-d102c53495f2", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("707dbd39-c6f6-5a91-a432-d102c53495f2", "2a35407a-3354-556b-9c2d-b16a6dcb5bd2")
        seedExerciseEquipment("2ed4071f-7f72-5fdc-9551-eb7cb896e015", "5a12e785-1773-59d6-8d56-b5516274ca38")
        seedExerciseEquipment("0dd33efa-9e67-51b0-a977-63de771b947e", "5a12e785-1773-59d6-8d56-b5516274ca38")
    }

    private fun seedExerciseEquipmentBatch1() {
        seedExerciseEquipment("cbe205fd-8784-5f1c-b577-af279ef584bb", "5a12e785-1773-59d6-8d56-b5516274ca38")
        seedExerciseEquipment("de7e0561-4597-5602-8e79-b34b16551b60", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("de7e0561-4597-5602-8e79-b34b16551b60", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("8a44c5e1-1a29-5014-bcd8-2840042418b7", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("8a44c5e1-1a29-5014-bcd8-2840042418b7", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("a1c712ac-a54e-5a87-a17e-cb7b3aeca4c1", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("a9ad4c7a-9a11-5815-9853-95adafb68c6b", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("42f9f70d-3032-5b92-91ed-8844b718cdf4", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("42f9f70d-3032-5b92-91ed-8844b718cdf4", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("fb8f58eb-6724-50b2-9363-6e38c9a4ca01", "50281eee-973e-568b-864c-8f82b8cc59eb")
        seedExerciseEquipment("d95d8324-5d3f-5e2d-a32d-fa2557887982", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("d95d8324-5d3f-5e2d-a32d-fa2557887982", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("5eeb83ec-8fd4-5e24-9728-97d1bf164e3b", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("5eeb83ec-8fd4-5e24-9728-97d1bf164e3b", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("b3eaafde-ced8-50ce-9542-5eb1e7ecbd58", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("b3eaafde-ced8-50ce-9542-5eb1e7ecbd58", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("501062f2-a304-5d3e-a030-3d8195c75b08", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("501062f2-a304-5d3e-a030-3d8195c75b08", "b98c3a1a-1972-5a55-be8f-d2ef5478e394")
        seedExerciseEquipment("6b4bf492-f6af-5f39-9375-4ac7d0863a40", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("6b4bf492-f6af-5f39-9375-4ac7d0863a40", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("5d51c529-0422-53fb-b08b-c737027e1426", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("05997e70-373b-5097-8be8-2037ea2b82f0", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("05997e70-373b-5097-8be8-2037ea2b82f0", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("853f5566-385a-5280-95e9-f768bea9d866", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("853f5566-385a-5280-95e9-f768bea9d866", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("ee11c194-45e2-5100-af83-500393829350", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("ee11c194-45e2-5100-af83-500393829350", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("50af7963-19cf-535e-b88d-57ed0d12b316", "b8c028ce-da84-5f09-a9d3-c4bf133e53f4")
        seedExerciseEquipment("6f74b34b-a36a-5dd7-a492-7381798847ca", "e499d26f-34df-5fd6-b159-0fe51d4861fd")
        seedExerciseEquipment("6f74b34b-a36a-5dd7-a492-7381798847ca", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("318fe537-3564-5f7a-b242-ad53506247f8", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("8481e25d-54cf-5d8a-9fc9-880bde0d7847", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("8481e25d-54cf-5d8a-9fc9-880bde0d7847", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("5fd63060-604d-5369-9119-df1b59aa9a71", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("ad35c535-6040-5273-bffa-6ec8bc3545da", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("30fe7001-05f1-5747-bc50-f25d077483f2", "1f1d4de6-b06a-52d9-9de7-6aac045df7e3")
        seedExerciseEquipment("a782442b-e605-525f-b2a7-581b2791121f", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("a782442b-e605-525f-b2a7-581b2791121f", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("59cc95b7-a120-58a1-a2f5-e2951475f954", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
    }

    private fun seedExerciseEquipmentBatch2() {
        seedExerciseEquipment("7298853d-3859-58bd-8f23-95078f371671", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("7298853d-3859-58bd-8f23-95078f371671", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("f00740be-b152-5a8d-a55e-9e3dca8019be", "b00c5efa-4686-5d1c-87d4-da029115ca70")
        seedExerciseEquipment("f00740be-b152-5a8d-a55e-9e3dca8019be", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("cd325c04-2c2e-5449-892f-4f10e0389d64", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("0e357f3b-43bf-5c10-a5d6-682aea0d7cb5", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("85ce2930-b984-5ad9-a4a0-1a5dcd65ff5b", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("85ce2930-b984-5ad9-a4a0-1a5dcd65ff5b", "d89a1979-a3b5-5d76-803e-7ce5e5a1b4cb")
        seedExerciseEquipment("2331302d-529c-5b04-aad0-3beca0a892e7", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("2331302d-529c-5b04-aad0-3beca0a892e7", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("acbe35b1-de7b-58b5-9ddf-155a01d98de1", "b00c5efa-4686-5d1c-87d4-da029115ca70")
        seedExerciseEquipment("acbe35b1-de7b-58b5-9ddf-155a01d98de1", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("acbe35b1-de7b-58b5-9ddf-155a01d98de1", "ddf5aef1-56bc-58a4-bf62-c0efbf5d6871")
        seedExerciseEquipment("77425758-2d20-5b95-afe2-8f667ad981a0", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("77425758-2d20-5b95-afe2-8f667ad981a0", "ddf5aef1-56bc-58a4-bf62-c0efbf5d6871")
        seedExerciseEquipment("28afaee1-db71-5ad8-8853-c1b38241bfcf", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("28afaee1-db71-5ad8-8853-c1b38241bfcf", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("14a700bc-ccab-579f-ae06-fadea778b186", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("14a700bc-ccab-579f-ae06-fadea778b186", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("a4a4d1af-7a83-5977-96b8-5a662f9c5d40", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("a4a4d1af-7a83-5977-96b8-5a662f9c5d40", "b98c3a1a-1972-5a55-be8f-d2ef5478e394")
        seedExerciseEquipment("b194b7cd-26c6-56f9-8bcf-07a9991f7c48", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("b194b7cd-26c6-56f9-8bcf-07a9991f7c48", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
        seedExerciseEquipment("9fccc47b-c8a7-5b2d-ac76-1fef5e9859b1", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("9fccc47b-c8a7-5b2d-ac76-1fef5e9859b1", "b98c3a1a-1972-5a55-be8f-d2ef5478e394")
        seedExerciseEquipment("4b9332c5-1870-5aec-8c3d-ef432ca8e622", "b00c5efa-4686-5d1c-87d4-da029115ca70")
        seedExerciseEquipment("4b9332c5-1870-5aec-8c3d-ef432ca8e622", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("4b9332c5-1870-5aec-8c3d-ef432ca8e622", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("404c7144-34a9-5c97-92aa-4aff27a073f7", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("1201d468-c410-515a-bb12-e0923e7c4659", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("1201d468-c410-515a-bb12-e0923e7c4659", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("1201d468-c410-515a-bb12-e0923e7c4659", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("1201d468-c410-515a-bb12-e0923e7c4659", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("6feaf01a-55a1-5c5c-9049-558b7a6eafc1", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("6feaf01a-55a1-5c5c-9049-558b7a6eafc1", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("9ce45737-bbe5-53e7-9cca-783655e85241", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("9ce45737-bbe5-53e7-9cca-783655e85241", "f598eb88-83d8-5672-bdfa-0b80f6f1d498")
        seedExerciseEquipment("778fba82-a707-534a-a2aa-e0a2f78594ef", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("7fd3a042-f71f-519c-b360-3b7274b41b16", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("7fd3a042-f71f-519c-b360-3b7274b41b16", "cdca829a-a389-5574-a173-4b5ba9f63bc5")
    }

    private fun seedExerciseEquipmentBatch3() {
        seedExerciseEquipment("c90339d6-640d-5734-b102-e5afe9d11118", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("c90339d6-640d-5734-b102-e5afe9d11118", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("c90339d6-640d-5734-b102-e5afe9d11118", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("922cc73f-3d8a-5b90-8f8b-489e18eeba5b", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("922cc73f-3d8a-5b90-8f8b-489e18eeba5b", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("922cc73f-3d8a-5b90-8f8b-489e18eeba5b", "f06c9206-1ae5-5991-80d1-8aa34cbd5125")
        seedExerciseEquipment("f0b77e93-6708-51ed-94ca-b496bd5c54b5", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("aa9e2059-4dfa-5d05-b2c9-7b5c33a5ae5e", "e499d26f-34df-5fd6-b159-0fe51d4861fd")
        seedExerciseEquipment("cda428ec-9108-5980-848e-8908a3163330", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("a52b3544-192e-57dd-973f-d52b201179a2", "dee340b0-82e5-58cb-bcfa-1ba830a75a06")
        seedExerciseEquipment("4e233d96-eb4c-5e7f-8898-3c376c6f3d32", "12b39133-d575-5f02-9d31-2cf794792ead")
        seedExerciseEquipment("c1d8c32d-931a-581d-a84b-990bcbdf1b50", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("69c89438-39b7-5f4c-8674-c40fc8158504", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("7cb71d0f-35ad-536d-b24f-29c2063e4a9d", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("7cb71d0f-35ad-536d-b24f-29c2063e4a9d", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("ad140972-5ace-538a-bfaf-fcdb0fc53726", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("ad140972-5ace-538a-bfaf-fcdb0fc53726", "cd21de91-9310-581d-aeae-c20bca2bc629")
        seedExerciseEquipment("ac9d7754-9cb3-54d9-a5e8-32b3438554d5", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("d6873a49-4db6-554a-9a0f-a690286de4a6", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("d6873a49-4db6-554a-9a0f-a690286de4a6", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("763d1380-633b-5876-b587-b668928da4f4", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("763d1380-633b-5876-b587-b668928da4f4", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("9ce08743-09da-50af-a8f7-2212b2caaf67", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("7f3f9270-5a28-513e-99c0-0dd6eb2c5adc", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("7f3f9270-5a28-513e-99c0-0dd6eb2c5adc", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("d6dbd523-c081-57fb-868a-492cc503284f", "745341f9-46a5-5d4d-98bc-59472c97c71b")
        seedExerciseEquipment("fa3454ba-cd22-5186-ab38-4e8b91dc27d7", "745341f9-46a5-5d4d-98bc-59472c97c71b")
        seedExerciseEquipment("2e626b00-1bd1-5507-b52c-1294997c40c1", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("41ca39e6-e04d-5a78-a801-8c079cb36290", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("41ca39e6-e04d-5a78-a801-8c079cb36290", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("41ca39e6-e04d-5a78-a801-8c079cb36290", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("0e4a214c-c322-5d73-9415-e4dacbc2bde9", "28257dd6-9907-5571-ab10-3730ec817e17")
        seedExerciseEquipment("26dc4363-acfa-5b24-b9d4-1dffe14491e0", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("26dc4363-acfa-5b24-b9d4-1dffe14491e0", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("ae9d1a74-773b-5fc7-9119-a3d1a901b8c8", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("39d5879c-a3ac-5790-bbc6-1aa3f4a7d316", "ec5bfb1c-95e7-5828-b187-2bac37e1eb4c")
        seedExerciseEquipment("fa858e71-5efa-5400-8c15-02ec4789fda6", "f5935721-3eef-5943-b053-39c55152774c")
        seedExerciseEquipment("27f8d5df-626a-580e-965a-2e237bef14aa", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("4766745c-bf63-549c-97e6-64579959b133", "9cf6a791-c486-5aea-8dd0-c6d9618eda7a")
        seedExerciseEquipment("2ad8248a-16d3-5595-83df-7fb68e7493d7", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
    }

    private fun seedExerciseEquipmentBatch4() {
        seedExerciseEquipment("2ad8248a-16d3-5595-83df-7fb68e7493d7", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("99ba4f38-55e6-53c5-b44a-ca93aadb4dcc", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("99ba4f38-55e6-53c5-b44a-ca93aadb4dcc", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("8a312b00-55d6-5ae7-9dac-b024d56faa7b", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("8a312b00-55d6-5ae7-9dac-b024d56faa7b", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("b16b3c98-3a8b-5280-a28e-e98dae1f0e6d", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("b16b3c98-3a8b-5280-a28e-e98dae1f0e6d", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("8ed001dc-3a6e-5312-b6b5-f9b3f972e0e4", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("8ed001dc-3a6e-5312-b6b5-f9b3f972e0e4", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("e32e0151-a9af-5627-92ac-cc0a0ef011a8", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("e32e0151-a9af-5627-92ac-cc0a0ef011a8", "2a35407a-3354-556b-9c2d-b16a6dcb5bd2")
        seedExerciseEquipment("8476a337-d4be-53fc-b73a-90443486fa61", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("8476a337-d4be-53fc-b73a-90443486fa61", "2a35407a-3354-556b-9c2d-b16a6dcb5bd2")
        seedExerciseEquipment("f5cdf847-6ec2-5ec5-81b6-64e22893075c", "ee524017-7e84-5822-a5e8-1dc1c67b3878")
        seedExerciseEquipment("f5cdf847-6ec2-5ec5-81b6-64e22893075c", "b98c3a1a-1972-5a55-be8f-d2ef5478e394")
        seedExerciseEquipment("60857cb5-95f1-5e96-9f7c-a912c2aecbb7", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("60857cb5-95f1-5e96-9f7c-a912c2aecbb7", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("2dbc1411-fce5-54ca-a55c-c7e275960ee8", "21ba1ccc-c6d6-5c22-93fc-82b2b081fa01")
        seedExerciseEquipment("2dbc1411-fce5-54ca-a55c-c7e275960ee8", "a5a04939-f923-5184-9b6a-3881a9eaaaa3")
        seedExerciseEquipment("46019347-494f-5e59-ae64-37cab695af7b", "2bde466c-37f3-515c-8f13-dd363396c3dd")
        seedExerciseEquipment("5814c728-823d-5553-a536-4184b8643403", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("5814c728-823d-5553-a536-4184b8643403", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("322e5f12-5e94-5f44-a3ba-ba5ebac6e140", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("322e5f12-5e94-5f44-a3ba-ba5ebac6e140", "5ea74e33-ddbe-54b9-bc68-8f0706b60357")
        seedExerciseEquipment("f59ca334-ebc3-5d1a-ab8a-894d30759186", "1ae2d82e-5d4f-5fff-a6a0-1c1eda03da7c")
        seedExerciseEquipment("bf2ecc6d-891a-5139-b2b2-785889c44854", "f5ae0440-8f58-592a-af73-bc8fce5834e8")
        seedExerciseEquipment("6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf", "79b1a0b9-c470-55a9-b7a9-7842077ec0e0")
        seedExerciseEquipment("6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf", "acae8a75-e01d-56d2-a988-09d6fd635f02")
        seedExerciseEquipment("6c00e49a-bfb6-5b68-94d8-10cb3a99c6cf", "075aa15f-8c15-5977-9ffc-f2349a36f65b")
        seedExerciseEquipment("da2037c4-1eb1-566f-9ce7-ddfb020fc49b", "1f46bef4-fb3c-53d7-8154-f9b6dbb48a24")
        seedExerciseEquipment("da2037c4-1eb1-566f-9ce7-ddfb020fc49b", "82746ac5-02c6-5bf2-856b-f529a6e0d86a")
        seedExerciseEquipment("32416061-25a8-525e-b338-d5b12e43545c", "767cb4a8-03b9-576e-82bb-e40a83f16b4d")
        seedExerciseEquipment("d1e70a83-9e4d-54bd-b5c9-7e89cc1e0b04", "767cb4a8-03b9-576e-82bb-e40a83f16b4d")
        seedExerciseEquipment("ca53839c-cdfd-5f5a-82c1-1b8d92b71617", "767cb4a8-03b9-576e-82bb-e40a83f16b4d")
    }

}
