-- Eliminar tablas que dependen de claves foráneas primero
DROP TABLE IF EXISTS Inscripcion;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS Horario;
DROP TABLE IF EXISTS Curso;
DROP TABLE IF EXISTS Prerrequisito;
DROP TABLE IF EXISTS Correquisito;

-- Eliminar tablas principales
DROP TABLE IF EXISTS Estudiante;
DROP TABLE IF EXISTS Profesor;
DROP TABLE IF EXISTS Materia;
DROP TABLE IF EXISTS Sala;

-- Eliminar tablas auxiliares
DROP TABLE IF EXISTS DiasSemana;
DROP TABLE IF EXISTS EstadoCurso;

-- TABLAS AUXILIARES

-- Tabla para los días de la semana
CREATE TABLE DiasSemana (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(20) NOT NULL
);

-- Tabla para los estados de los cursos
CREATE TABLE EstadoCurso (
    id INT PRIMARY KEY IDENTITY(1,1),
    estado VARCHAR(50) NOT NULL
);

-- TABLAS PRINCIPALES

-- Tabla Estudiante
CREATE TABLE Estudiante (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL,
	creditos_max INT NOT NULL,
);

-- Tabla Profesor
CREATE TABLE Profesor (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL
);

-- Tabla Materia (sin claves foráneas de prerrequisito y corequisito)
CREATE TABLE Materia (
    id INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    creditos INT NOT NULL
);

-- Tabla Sala
CREATE TABLE Sala (
    id INT PRIMARY KEY IDENTITY(1,1),
    ubicacion VARCHAR(100) NOT NULL,
    capacidad INT NOT NULL,
    tipo VARCHAR(50)
);

-- Tabla Curso
CREATE TABLE Curso (
    id INT PRIMARY KEY IDENTITY(1,1),
	cupos INT NOT NULL,
    capacidad INT NOT NULL,
    materia_id INT NOT NULL,
    sala_id INT NULL, -- Cambiado para permitir valores nulos
    estado_id INT NOT NULL,
    FOREIGN KEY (materia_id) REFERENCES Materia(id) ON DELETE CASCADE,
    FOREIGN KEY (sala_id) REFERENCES Sala(id) ON DELETE SET NULL, -- Acción ON DELETE SET NULL ahora es válida
    FOREIGN KEY (estado_id) REFERENCES EstadoCurso(id)
);


-- Relación entre Curso y Estudiante (Inscripciones)
CREATE TABLE Inscripcion (
    id INT PRIMARY KEY IDENTITY(1,1),
    estudiante_id INT NOT NULL,
    curso_id INT NOT NULL,
    ha_aprobado BIT DEFAULT 0,
    FOREIGN KEY (estudiante_id) REFERENCES Estudiante(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES Curso(id) ON DELETE CASCADE,
    UNIQUE(estudiante_id, curso_id)
);

-- Tabla Prerrequisito
CREATE TABLE Prerrequisito (
    MateriaID INT,
    PrerrequisitoID INT,
    FOREIGN KEY (MateriaID) REFERENCES Materia(ID) ON DELETE CASCADE,
    FOREIGN KEY (PrerrequisitoID) REFERENCES Materia(ID) ON DELETE NO ACTION,
    PRIMARY KEY (MateriaID, PrerrequisitoID)
);

-- Tabla Correquisito
CREATE TABLE Correquisito (
    MateriaID INT,
    CorrequisitoID INT,
    FOREIGN KEY (MateriaID) REFERENCES Materia(ID) ON DELETE CASCADE,
    FOREIGN KEY (CorrequisitoID) REFERENCES Materia(ID) ON DELETE NO ACTION,
    PRIMARY KEY (MateriaID, CorrequisitoID)
);

-- Relación entre Curso y Profesor (Asignaciones)
CREATE TABLE Asignacion (
    id INT PRIMARY KEY IDENTITY(1,1),
    profesor_id INT NOT NULL,
    curso_id INT NOT NULL,
    FOREIGN KEY (profesor_id) REFERENCES Profesor(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES Curso(id) ON DELETE CASCADE,
    UNIQUE(profesor_id, curso_id)
);

-- Horarios de los cursos
CREATE TABLE Horario (
    id INT PRIMARY KEY IDENTITY(1,1),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    dia_semana_id INT NOT NULL,
    materia_id INT NOT NULL,
    sala_id INT NULL,
    FOREIGN KEY (dia_semana_id) REFERENCES DiasSemana(id),
    FOREIGN KEY (materia_id) REFERENCES Materia(id) ON DELETE CASCADE,
    FOREIGN KEY (sala_id) REFERENCES Sala(id) ON DELETE SET NULL -- Evitar ciclos
);


-- Inserciones en las tablas auxiliares

-- D�as de la semana
INSERT INTO DiasSemana (nombre) VALUES 
('Lunes'),
('Martes'),
('Miercoles'),
('Jueves'),
('Viernes'),
('Sabado');

-- Estados de los cursos
INSERT INTO EstadoCurso (estado) VALUES 
('Abierto'),
('Cerrado'),
('En progreso'),
('Finalizado');

-- Inserciones en las tablas principales

-- Estudiantes
INSERT INTO Estudiante (nombre, documento, correo, clave, creditos_max) VALUES
('Juan Pérez', '12345678', 'juan.perez@email.com', 'claveSegura1', 10),
('Ana Gómez', '87654321', 'ana.gomez@email.com', 'claveSegura2', 20),
('Pedro López', '23456789', 'pedro.lopez@email.com', 'claveSegura3', 21),
('Laura Martínez', '34567890', 'laura.martinez@email.com', 'claveSegura4', 22),
('Carlos Rodríguez', '45678901', 'carlos.rodriguez@email.com', 'claveSegura5', 10),
('María Fernández', '56789012', 'maria.fernandez@email.com', 'claveSegura6', 20),
('Luis Sánchez', '67890123', 'luis.sanchez@email.com', 'claveSegura7', 21),
('Elena Díaz', '78901234', 'elena.diaz@email.com', 'claveSegura8', 22),
('Javier Morales', '89012345', 'javier.morales@email.com', 'claveSegura9', 10),
('Sofía Jiménez', '90123456', 'sofia.jimenez@email.com', 'claveSegura10', 20),
('Andrés Torres', '01234567', 'andres.torres@email.com', 'claveSegura11', 21),
('Verónica Ruiz', '12345679', 'veronica.ruiz@email.com', 'claveSegura12', 22),
('Gabriel Castro', '23456780', 'gabriel.castro@email.com', 'claveSegura13', 10),
('Marta Ortega', '34567891', 'marta.ortega@email.com', 'claveSegura14', 20),
('David Herrera', '45678902', 'david.herrera@email.com', 'claveSegura15', 21),
('Clara Romero', '56789013', 'clara.romero@email.com', 'claveSegura16', 22),
('Sergio Vargas', '67890124', 'sergio.vargas@email.com', 'claveSegura17', 10),
('Isabel Méndez', '78901235', 'isabel.mendez@email.com', 'claveSegura18', 20),
('Fernando Cordero', '89012346', 'fernando.cordero@email.com', 'claveSegura19', 21),
('Carmen Silva', '90123457', 'carmen.silva@email.com', 'claveSegura20', 22),
('Raúl Paredes', '01234568', 'raul.paredes@email.com', 'claveSegura21', 10),
('Patricia León', '12345680', 'patricia.leon@email.com', 'claveSegura22', 20),
('Jorge Salas', '23456781', 'jorge.salas@email.com', 'claveSegura23', 21),
('Lucía Peña', '34567892', 'lucia.pena@email.com', 'claveSegura24', 22),
('Alejandro Moreno', '45678903', 'alejandro.moreno@email.com', 'claveSegura25', 10);


-- Profesores
INSERT INTO Profesor (nombre, apellido, documento, correo, clave) VALUES
('Carlos', 'Rodriguez', '11223344', 'carlos.rodriguez@email.com', 'claveProfe1'),
('Lucia', 'Martinez', '44332211', 'lucia.martinez@email.com', 'claveProfe2'),
('Samuel', 'Pérez', '55667788', 'samuel.perez@email.com', 'claveProfe3'),
('Maria', 'Gómez', '99887766', 'maria.gomez@email.com', 'claveProfe4'),
('Luis', 'Hernández', '33445566', 'luis.hernandez@email.com', 'claveProfe5'),
('María', 'López', '22334455', 'maria.lopez@email.com', 'claveProfe6'),
('Pedro', 'Sánchez', '11224488', 'pedro.sanchez@email.com', 'claveProfe7'),
('Sofía', 'Ramírez', '66778899', 'sofia.ramirez@email.com', 'claveProfe8'),
('Diego', 'Torres', '44556677', 'diego.torres@email.com', 'claveProfe9'),
('Elena', 'Vázquez', '55661788', 'elena.vazquez@email.com', 'claveProfe10'),
('Javier', 'Castillo', '77889900', 'javier.castillo@email.com', 'claveProfe11'),
('Clara', 'Morales', '22335577', 'clara.morales@email.com', 'claveProfe12'),
('Andrés', 'Rivas', '33448899', 'andres.rivas@email.com', 'claveProfe13'),
('Verónica', 'Mendoza', '44559900', 'veronica.mendoza@email.com', 'claveProfe14'),
('Francisco', 'Cruz', '55660011', 'francisco.cruz@email.com', 'claveProfe15'),
('Gabriel', 'Ortiz', '66771122', 'gabriel.ortiz@email.com', 'claveProfe16'),
('Natalia', 'Fernández', '77882233', 'natalia.fernandez@email.com', 'claveProfe17'),
('Juan', 'García', '88993344', 'juan.garcia@email.com', 'claveProfe18'),
('Ana', 'Suárez', '99004455', 'ana.suarez@email.com', 'claveProfe19'),
('Esteban', 'Ruiz', '11221133', 'esteban.ruiz@email.com', 'claveProfe20'),
('Laura', 'Gutiérrez', '22332244', 'laura.gutierrez@email.com', 'claveProfe21'),
('Pablo', 'Núñez', '33443355', 'pablo.nunez@email.com', 'claveProfe22'),
('Camila', 'Alvarez', '44554466', 'camila.alvarez@email.com', 'claveProfe23'),
('Daniel', 'Vargas', '55665577', 'daniel.vargas@email.com', 'claveProfe24'),
('Sara', 'Pineda', '66776688', 'sara.pineda@email.com', 'claveProfe25'),
('Rafael', 'Romero', '77887799', 'rafael.romero@email.com', 'claveProfe26'),
('Marta', 'Castro', '88998811', 'marta.castro@email.com', 'claveProfe27'),
('David', 'Molina', '99009922', 'david.molina@email.com', 'claveProfe28'),
('Elisa', 'Soto', '11223355', 'elisa.soto@email.com', 'claveProfe29'),
('Manuel', 'Cabrera', '22334466', 'manuel.cabrera@email.com', 'claveProfe30');


-- Materias

INSERT INTO Materia (nombre, descripcion, creditos) VALUES
('Cálculo Diferencial', 'Estudio de los conceptos básicos del cálculo.', 3),
('Lógica y Matemáticas Discretas', 'Fundamentos lógicos y matemáticas para ciencias de la computación.', 3),
('Introducción a la Programación', 'Conceptos fundamentales de la programación.', 3),
('Pensamiento Sistémico', 'Enfoque holístico para la resolución de problemas.', 3),
('Introducción a la Ingeniería', 'Conceptos fundamentalesde la ingeniería.', 2),
('Fundamentos Seguridad de la Información', 'Principios y conceptos clave de la seguridad informática.', 2),
('Arquitectura y Organización del Computador', 'Estructura interna y funcionamiento de los computadores.', 3),
('Electiva', 'Materia electiva de libre elección.', 2),
('Cálculo Integral', 'Integración y sus aplicaciones.', 3),
('Álgebra Lineal', 'Espacios vectoriales y matrices.', 3),
('Programación Avanzada', 'Técnicas avanzadas de programación.', 4),
('Gestión Financiera de Proyectos de TI', 'Principios financieros aplicados a proyectos tecnológicos.', 2),
('Proyecto de Diseño en Ingeniería 1', 'Desarrollo de proyectos de diseño en ingeniería.', 3),
('Significación Teológica', 'Estudios en teología y su significado.', 2),
('Probabilidad y Estadística', 'Modelos probabilísticos y análisis estadístico.', 3),
('Bases de Datos', 'Diseño, creación y gestión de bases de datos.', 4),
('Análisis y Diseño de Software', 'Técnicas y herramientas de análisis de software.', 3),
('Comunicación y Redes', 'Técnicas avanzadas de comunicación.', 4),
('Estructuras de Datos', 'Algoritmos y estructuras de datos.', 3),
('Sistemas de Información', 'Desarrollo y gestión de sistemas de información.', 3),
('Fundamentos de Ingeniería de Software', 'Principios y métodos de la ingeniería de software.', 3),
('Sistemas Operativos', 'Diseño y operación de sistemas operativos.', 4),
('Cálculo Vectorial', 'Extensión del cálculo en varias variables.', 3),
('Teoría de la Computación', 'Fundamentos teóricos de la computación.', 2),
('Gestión de Proyectos de Innovación y Emprendimiento', 'Desarrollo de proyectos innovadores en ingeniería.', 3),
('Proyecto de Innovacion y Emprendimiento', 'Desarrollo de proyectos innovadores en ingeniería.', 3),
('Sistemas Distribuidos', 'Arquitectura y diseño de sistemas distribuidos.', 3),
('Complementaria', 'Materia complementaria en el área de TI.', 2),
('Física Mecánica', 'Fundamentos de la mecánica en física.', 3),
('Ecuaciones Diferenciales', 'Modelado de sistemas dinámicos mediante ecuaciones diferenciales.', 3),
('Optimización y Simulación', 'Técnicas de optimización y simulación.', 2),
('Análisis de Algoritmos', 'Estudio del rendimiento y diseño de algoritmos.', 2),
('Desarrollo Web', 'Desarrollo de aplicaciones web.', 3),
('Introducción a la Computación Móvil', 'Desarrollo de aplicaciones móviles.', 3),
('Introducción a la Inteligencia Artificial', 'Conceptos básicos y aplicaciones de la IA.', 3),
('Fe y Compromiso del Ingeniero', 'Recalcar la fe y compromiso del ingeniero.', 2),
('Planeación de Proyectos TI', 'Planeación avanzada de proyectos tecnológicos.', 3),
('Arquitectura de Software', 'Diseño y construcción de software modular.', 3),
('Proyecto Social Universitario', 'Desarrollo de proyectos con impacto social.', 2),
('Etica en la era de la información', 'Mostrar conceptos de la etica en la era de la información.', 2),
('Analisis Numerico', 'Analisis de las matemáticas.', 3),
('Tecnologías Digitales Emergentes', 'Estudio de nuevas tecnologías digitales.', 2),
('Proyecto de grado', 'Proyecto de Grado.', 3),
('Construcción y Derecho Civil', 'Conceptos de construcción y legislación en ingeniería.', 2),
('Epistemología de la Ingeniería', 'Estudio filosófico de la ingeniería.', 2),
('Gerencia Estrategica de TI', 'Gerencia en el campo TI.', 2);


-- Salas
INSERT INTO Sala (ubicacion, capacidad, tipo) VALUES
('Edificio A, Aula 101', 30, 'Aula'),
('Edificio B, Aula 202', 50, 'Laboratorio'),
('Edificio C, Aula 303', 40, 'Aula'),
('Edificio D, Aula 404', 25, 'Salón'),
('Edificio E, Aula 505', 35, 'Aula'),
('Edificio F, Aula 606', 45, 'Laboratorio'),
('Edificio G, Aula 707', 20, 'Aula'),
('Edificio H, Aula 808', 60, 'Salón'),
('Edificio I, Aula 909', 55, 'Aula'),
('Edificio J, Aula 1010', 15, 'Laboratorio'),
('Edificio K, Aula 1111', 70, 'Aula'),
('Edificio L, Aula 1212', 80, 'Salón'),
('Edificio M, Aula 1313', 65, 'Aula'),
('Edificio N, Aula 1414', 40, 'Aula'),
('Edificio O, Aula 1515', 30, 'Laboratorio'),
('Edificio P, Aula 1616', 50, 'Salón'),
('Edificio Q, Aula 1717', 35, 'Aula'),
('Edificio R, Aula 1818', 25, 'Laboratorio'),
('Edificio S, Aula 1919', 60, 'Salón'),
('Edificio T, Aula 2020', 45, 'Aula'),
('Edificio U, Aula 2121', 55, 'Laboratorio'),
('Edificio V, Aula 2222', 20, 'Aula'),
('Edificio W, Aula 2323', 70, 'Salón');


-- Cursos (Primero aseg�rate de que existen materias, salas y estados)
INSERT INTO Curso (cupos,capacidad, materia_id, sala_id, estado_id) VALUES
(50,40, 1, 10, 1),  -- Materia 1
(55,50, 2, 8, 1),   -- Materia 2
(60,45, 3, 7, 1),   -- Materia 3
(60,60, 4, 6, 1),   -- Materia 4
(50,35, 5, 5, 1),   -- Materia 5
(45,30, 6, 4, 1),   -- Materia 6
(55,50, 7, 3, 1),   -- Materia 7
(60,45, 8, 2, 1),   -- Materia 8
(70,60, 9, 1, 1),   -- Materia 9
(50,40, 10, 12, 1), -- Materia 10
(40,35, 11, 11, 1), -- Materia 11
(55,50, 12, 10, 1), -- Materia 12
(35,30, 13, 9, 1),  -- Materia 13
(50,45, 14, 8, 1),  -- Materia 14
(65,60, 15, 7, 1),  -- Materia 15
(50,40, 16, 6, 1),  -- Materia 16
(45,35, 17, 5, 1),  -- Materia 17
(45,30, 18, 4, 1),  -- Materia 18
(50,50, 19, 3, 1),  -- Materia 19
(50,45, 20, 2, 1),  -- Materia 20
(65,60, 21, 1, 1),  -- Materia 21
(50,40, 22, 12, 1), -- Materia 22
(45,35, 23, 11, 1), -- Materia 23
(50,50, 24, 10, 1), -- Materia 24
(45,30, 25, 9, 1),  -- Materia 25
(50,45, 26, 8, 1),  -- Materia 26
(65,60, 27, 7, 1),  -- Materia 27
(50,40, 28, 6, 1),  -- Materia 28
(45,35, 29, 5, 1),  -- Materia 29
(45,30, 30, 4, 1),  -- Materia 30
(50,50, 31, 3, 1),  -- Materia 31
(50,45, 32, 2, 1),  -- Materia 32
(60,60, 33, 1, 1),  -- Materia 33
(50,40, 34, 12, 1), -- Materia 34
(45,35, 35, 11, 1), -- Materia 35
(50,50, 36, 10, 1), -- Materia 36
(45,30, 37, 9, 1),  -- Materia 37
(50,45, 38, 8, 1),  -- Materia 38
(65,60, 39, 7, 1),  -- Materia 39
(50,40, 40, 6, 1),  -- Materia 40
(45,35, 41, 5, 1),  -- Materia 41
(45,30, 42, 4, 1),  -- Materia 42
(50,50, 43, 3, 1),  -- Materia 43
(50,45, 44, 2, 1),  -- Materia 44
(65,60, 45, 1, 1),  -- Materia 45
(50,40, 46, 12, 1), -- Materia 46
(45,35, 1, 11, 1),  -- Repetida Materia 1
(50,50, 2, 10, 1),  -- Repetida Materia 2
(45,30, 3, 9, 1),   -- Repetida Materia 3
(50,45, 4, 8, 1),   -- Repetida Materia 4
(65,60, 5, 7, 1),   -- Repetida Materia 5
(50,40, 6, 6, 1),   -- Repetida Materia 6
(45,35, 7, 5, 1),   -- Repetida Materia 7
(50,50, 8, 4, 1),   -- Repetida Materia 8
(45,30, 9, 3, 1),   -- Repetida Materia 9
(65,60, 10, 2, 1),  -- Repetida Materia 10
(50,45, 11, 1, 1),  -- Repetida Materia 11
(50,50, 12, 12, 1), -- Repetida Materia 12
(50,40, 13, 11, 1), -- Repetida Materia 13
(45,35, 14, 10, 1), -- Repetida Materia 14
(45,30, 15, 9, 1),  -- Repetida Materia 15
(65,60, 16, 8, 1),  -- Repetida Materia 16
(50,45, 17, 7, 1),  -- Repetida Materia 17
(50,50, 18, 6, 1),  -- Repetida Materia 18
(50,40, 19, 5, 1),  -- Repetida Materia 19
(45,35, 20, 4, 1),  -- Repetida Materia 20
(45,30, 21, 3, 1),  -- Repetida Materia 21
(65,60, 22, 2, 1),  -- Repetida Materia 22
(50,45, 23, 1, 1),  -- Repetida Materia 23
(50,50, 24, 12, 1), -- Repetida Materia 24
(50,40, 25, 11, 1), -- Repetida Materia 25
(45,35, 26, 10, 1), -- Repetida Materia 26
(45,30, 27, 9, 1),  -- Repetida Materia 27
(65,60, 28, 8, 1),  -- Repetida Materia 28
(50,45, 29, 7, 1),  -- Repetida Materia 29
(50,50, 30, 6, 1),  -- Repetida Materia 30
(50,40, 31, 5, 1),  -- Repetida Materia 31
(45,35, 32, 4, 1),  -- Repetida Materia 32
(45,30, 33, 3, 1),  -- Repetida Materia 33
(65,60, 34, 2, 1);  -- Repetida Materia 34



-- Relaci�n entre Curso y Estudiante (Inscripciones)
INSERT INTO Inscripcion (estudiante_id, curso_id, ha_aprobado) VALUES
-- Estudiante 1
(1, 1, 0), (1, 6, 0), (1, 11, 0), (1, 16, 0), (1, 21, 0), (1,5,1),
-- Estudiante 2
(2, 2, 0), (2, 7, 0), (2, 12, 0), (2, 17, 0), (2, 22, 0),
-- Estudiante 3
(3, 3, 0), (3, 8, 0), (3, 13, 0), (3, 18, 0), (3, 23, 0),
-- Estudiante 4
(4, 4, 0), (4, 9, 0), (4, 14, 0), (4, 19, 0), (4, 24, 0),
-- Estudiante 5
(5, 5, 0), (5, 10, 0), (5, 15, 0), (5, 20, 0), (5, 25, 0),
-- Estudiante 6
(6, 26, 0), (6, 31, 0), (6, 36, 0), (6, 41, 0), (6, 46, 0),
-- Estudiante 7
(7, 47, 0), (7, 52, 0), (7, 57, 0), (7, 62, 0), (7, 67, 0),
-- Estudiante 8
(8, 48, 0), (8, 53, 0), (8, 58, 0), (8, 63, 0), (8, 68, 0),
-- Estudiante 9
(9, 49, 0), (9, 54, 0), (9, 59, 0), (9, 64, 0), (9, 69, 0),
-- Estudiante 10
(10, 50, 0), (10, 55, 0), (10, 60, 0), (10, 65, 0), (10, 70, 0),
-- Estudiante 11
(11, 1, 0), (11, 27, 0), (11, 32, 0), (11, 37, 0), (11, 42, 0),
-- Estudiante 12
(12, 2, 0), (12, 28, 0), (12, 33, 0), (12, 38, 0), (12, 43, 0),
-- Estudiante 13
(13, 3, 0), (13, 29, 0), (13, 34, 0), (13, 39, 0), (13, 44, 0),
-- Estudiante 14
(14, 4, 0), (14, 30, 0), (14, 35, 0), (14, 40, 0), (14, 45, 0),
-- Estudiante 15
(15, 5, 0), (15, 31, 0), (15, 36, 0), (15, 41, 0), (15, 46, 0),
-- Estudiante 16
(16, 51, 0), (16, 56, 0), (16, 61, 0), (16, 66, 0), (16, 71, 0),
-- Estudiante 17
(17, 52, 0), (17, 57, 0), (17, 62, 0), (17, 67, 0), (17, 72, 0),
-- Estudiante 18
(18, 53, 0), (18, 58, 0), (18, 63, 0), (18, 68, 0), (18, 73, 0),
-- Estudiante 19
(19, 54, 0), (19, 59, 0), (19, 64, 0), (19, 69, 0), (19, 74, 0),
-- Estudiante 20
(20, 55, 0), (20, 60, 0), (20, 65, 0), (20, 70, 0), (20, 75, 0),
-- Estudiante 21
(21, 6, 0), (21, 11, 0), (21, 16, 0), (21, 21, 0), (21, 76, 0),
-- Estudiante 22
(22, 7, 0), (22, 12, 0), (22, 17, 0), (22, 22, 0), (22, 77, 0),
-- Estudiante 23
(23, 8, 0), (23, 13, 0), (23, 18, 0), (23, 23, 0), (23, 78, 0),
-- Estudiante 24
(24, 9, 0), (24, 14, 0), (24, 19, 0), (24, 24, 0), (24, 79, 0),
-- Estudiante 25
(25, 10, 0), (25, 15, 0), (25, 20, 0), (25, 25, 0), (25, 80, 0), (25,9,1);

INSERT INTO Prerrequisito(MateriaID , PrerrequisitoID) VALUES
(9, 1),
(24,2),(19,2),(7,2),
(11,3),(18,3),(7,3),
(20,4),
(13,5),
(27,6),
(22,7),
(15,9),(23,9),(30,9),
(23,10),(30,10),(31,10),
(24,11),(19,11),(16,11),(17,11),
(25,12),
(26,13),(14,13),
(31,15),
(33,16),(34,16),
(21,17),(27,17),
(27,18),(34,18),(33,18),
(35,19),(32,19),
(37,20),(26,20),
(34,21),(39,21),(33,21),
(27,22),
(32,24),(33,24),
(26,25),(39,26),(37,26),
(46,26),(43,26),(36,26),
(39,27),
(41,30),
(38,33),
(38,34),
(43,37);

INSERT INTO Correquisito (MateriaID , CorrequisitoID) VALUES
(15,22),(22,15),
(16,20),(20,16),
(43,38),(38,43);


-- Relaci�n entre Curso y Profesor (Asignaciones)
INSERT INTO Asignacion (profesor_id, curso_id) VALUES
-- Profesor 1
(1, 1), (1, 37), (1, 72),
-- Profesor 2
(2, 15), (2, 48), (2, 79),
-- Profesor 3
(3, 6), (3, 41), (3, 68),
-- Profesor 4
(4, 22), (4, 53), (4, 80),
-- Profesor 5
(5, 9), (5, 35), (5, 61),
-- Profesor 6
(6, 18), (6, 44), (6, 70),
-- Profesor 7
(7, 3), (7, 29), (7, 55),
-- Profesor 8
(8, 12), (8, 38), (8, 64),
-- Profesor 9
(9, 25), (9, 51), (9, 77),
-- Profesor 10
(10, 7), (10, 33), (10, 59),
-- Profesor 11
(11, 20), (11, 46), (11, 73),
-- Profesor 12
(12, 2), (12, 28), (12, 54),
-- Profesor 13
(13, 16), (13, 42), (13, 69),
-- Profesor 14
(14, 10), (14, 36), (14, 62),
-- Profesor 15
(15, 24), (15, 50), (15, 76),
-- Profesor 16
(16, 5), (16, 31), (16, 57),
-- Profesor 17
(17, 14), (17, 40), (17, 66),
-- Profesor 18
(18, 27), (18, 52), (18, 78),
-- Profesor 19
(19, 8), (19, 34), (19, 60),
-- Profesor 20
(20, 21), (20, 47), (20, 74),
-- Profesor 21
(21, 4), (21, 30),
-- Profesor 22
(22, 17), (22, 43),
-- Profesor 23
(23, 11), (23, 39),
-- Profesor 24
(24, 26), (24, 56),
-- Profesor 25
(25, 13), (25, 45),
-- Profesor 26
(26, 32), (26, 58),
-- Profesor 27
(27, 19), (27, 65),
-- Profesor 28
(28, 23), (28, 49),
-- Profesor 29
(29, 63), (29, 75),
-- Profesor 30
(30, 67), (30, 71);


-- Horarios de los cursos
INSERT INTO Horario (hora_inicio, hora_fin, dia_semana_id, materia_id, sala_id) VALUES
('20:00', '22:00', 3, 43, 3),   -- Materia 43, Lunes
('20:00', '22:00', 1, 43, 1),   -- Materia 43, Lunes
('08:00', '10:00', 1, 1, 1),   -- Materia 1, Lunes
('08:00', '10:00', 2, 1, 1),   -- Materia 1, Lunes
('10:00', '12:00', 3, 2, 2),   -- Materia 2, Miércoles
('14:00', '16:00', 2, 3, 3),   -- Materia 3, Martes
('16:00', '18:00', 4, 4, 4),   -- Materia 4, Jueves
('08:00', '10:00', 1, 5, 5),   -- Materia 5, Lunes
('10:00', '12:00', 3, 6, 6),   -- Materia 6, Miércoles
('14:00', '16:00', 5, 7, 7),   -- Materia 7, Viernes
('16:00', '18:00', 6, 8, 8),   -- Materia 8, Sábado
('08:00', '10:00', 1, 9, 9),   -- Materia 9, Lunes
('10:00', '12:00', 2, 10, 10), -- Materia 10, Martes
('08:00', '10:00', 1, 11, 1), -- Materia 11, Lunes
('10:00', '12:00', 3, 12, 2), -- Materia 12, Miércoles
('14:00', '16:00', 2, 13, 3), -- Materia 13, Martes
('16:00', '18:00', 4, 14, 4), -- Materia 14, Jueves
('08:00', '10:00', 1, 15, 5), -- Materia 15, Lunes
('10:00', '12:00', 3, 16, 6), -- Materia 16, Miércoles
('14:00', '16:00', 5, 17, 7), -- Materia 17, Viernes
('16:00', '18:00', 6, 18,8), -- Materia 18, Sábado
('08:00', '10:00', 1, 19, 9), -- Materia 19, Lunes
('10:00', '12:00', 2, 20, 10), -- Materia 20, Martes
('08:00', '10:00', 1, 21, 1), -- Materia 21, Lunes
('10:00', '12:00', 3, 22, 2), -- Materia 22, Miércoles
('14:00', '16:00', 2, 23, 3), -- Materia 23, Martes
('16:00', '18:00', 4, 24, 4), -- Materia 24, Jueves
('08:00', '10:00', 1, 25, 5), -- Materia 25, Lunes
('10:00', '12:00', 3, 26, 6), -- Materia 26, Miércoles
('14:00', '16:00', 5, 27, 7), -- Materia 27, Viernes
('16:00', '18:00', 6, 28, 8), -- Materia 28, Sábado
('08:00', '10:00', 1, 29, 9), -- Materia 29, Lunes
('10:00', '12:00', 2, 30, 10), -- Materia 30, Martes
('08:00', '10:00', 1, 31, 11), -- Materia 31, Lunes
('10:00', '12:00', 3, 32, 12), -- Materia 32, Miércoles
('14:00', '16:00', 2, 33, 13), -- Materia 33, Martes
('16:00', '18:00', 4, 34, 13), -- Materia 34, Jueves
('08:00', '10:00', 1, 35, 5), -- Materia 35, Lunes
('10:00', '12:00', 3, 36, 6), -- Materia 36, Miércoles
('14:00', '16:00', 5, 37, 7), -- Materia 37, Viernes
('16:00', '18:00', 6, 38, 8), -- Materia 38, Sábado
('08:00', '10:00', 1, 39, 3), -- Materia 39, Lunes
('10:00', '12:00', 2, 40, 4), -- Materia 40, Martes
('08:00', '10:00', 1, 41, 1), -- Materia 41, Lunes
('10:00', '12:00', 3, 42, 2); -- Materia 42, Miércoles
/*
SELECT * FROM dbo.Inscripcion
SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Estudiante'

SELECT c.id AS curso_id, m.nombre AS nombre_materia, i.ha_aprobado
FROM Inscripcion i
JOIN Curso c ON i.curso_id = c.id
JOIN Materia m ON c.materia_id = m.id
WHERE i.estudiante_id = '2'
ORDER BY i.ha_aprobado, m.nombre;

SELECT e.id, e.nombre, e.creditos_max FROM Estudiante e WHERE e.id = 1;


SELECT COUNT(*) FROM Estudiante WHERE correo = 'juan.pe4rez@email.com' AND clave = 'claveSegura1'

SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre
FROM Curso c
JOIN Inscripcion i ON c.id = i.curso_id
JOIN Materia m ON c.materia_id = m.id
WHERE i.estudiante_id = 1 AND i.ha_aprobado = 0

SELECT c.id, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre
FROM Curso c
JOIN Inscripcion i ON c.id = i.curso_id
JOIN Materia m ON c.materia_id = m.id
WHERE i.estudiante_id = 1 AND i.ha_aprobado = 1

use master;

SELECT * FROM dbo.Sala
SELECT * FROM Profesor
SELECT * FROM Estudiante
SELECT * FROM Inscripcion
SELECT * FROM Curso

ROLLBACK

SELECT h.hora_inicio, h.hora_fin FROM Horario h WHERE h.materia_id = 1;

SELECT c.id AS curso_id, 
       c.materia_id, 
       m.nombre AS nombre_materia,
       STRING_AGG(CONCAT(d.nombre, ' ', FORMAT(h.hora_inicio, 'HH:mm'), '-', FORMAT(h.hora_fin, 'HH:mm')), ', ') AS horarios
FROM Curso c 
JOIN Materia m ON c.materia_id = m.id 
JOIN Horario h ON c.materia_id = h.materia_id 
JOIN DiasSemana d ON h.dia_semana_id = d.id
WHERE m.id = 1
GROUP BY c.id, c.materia_id, m.nombre;


SELECT h.hora_inicio, h.hora_fin
FROM Horario h
JOIN Curso c ON h.materia_id = c.materia_id
WHERE c.id = 1

SELECT * FROM Curso WHERE id = 47





/*SELECT id, nombre, correo, creditos_max FROM Estudiante;



SELECT COLUMN_NAME 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Materia';




SELECT c.id, c.cupos, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre
FROM Curso c
JOIN Inscripcion i ON c.id = i.curso_id	
JOIN Materia m ON c.materia_id = m.id
WHERE i.estudiante_id = 1 AND i.ha_aprobado = 0;

SELECT hora_inicio, hora_fin
FROM Horario
WHERE curso_id = 1

SELECT s.id, s.ubicacion, s.capacidad
FROM Sala s
JOIN Curso c
ON c.sala_id = s.id
WHERE c.id =1

SELECT c.id, c.capacidad, c.cupos, m.id AS materia_id, m.nombre AS materia_nombre
FROM Curso c
JOIN Inscripcion i ON c.id = i.curso_id
JOIN Materia m ON c.materia_id = m.id
WHERE i.estudiante_id = 1 AND i.ha_aprobado = 1

SELECT i.estudiante_id, c.id AS curso_id 
FROM Inscripcion i
JOIN Curso c ON i.curso_id = c.id
WHERE i.ha_aprobado = 1;

SELECT c.id, c.cupos, c.capacidad, m.id AS materia_id, m.nombre AS materia_nombre, s.id AS sala_id, s.ubicacion
FROM Curso c
LEFT JOIN Materia m ON c.materia_id = m.id
LEFT JOIN Sala s ON c.sala_id = s.id
WHERE c.id = 1

SELECT p.id, p.nombre, p.apellido FROM Profesor p
JOIN Asignacion a ON p.id = a.profesor_id
WHERE a.curso_id = 1

SELECT e.id, e.nombre FROM Estudiante e
JOIN Inscripcion i ON e.id = i.estudiante_id
WHERE i.curso_id = 2

SELECT * FROM Materia WHERE id=48
SELECT m.* FROM Materia m JOIN Prerrequisito p ON m.id = p.PrerrequisitoID WHERE p.MateriaID = 11
SELECT m.* FROM Materia m JOIN Correquisito c ON m.id = c.CorrequisitoID WHERE c.MateriaID =15

SELECT * FROM Profesor
SELECT * FROM Materia
SELECT m.id, m.nombre, m.descripcion, m.creditos,
       pr.nombre AS PreRequisito,
       cr.nombre AS CoRequisito
FROM dbo.Materia m
LEFT JOIN dbo.Prerrequisito p ON m.id = p.MateriaID
LEFT JOIN dbo.Materia pr ON p.PrerrequisitoID = pr.id
LEFT JOIN dbo.Correquisito c ON m.id = c.MateriaID
LEFT JOIN dbo.Materia cr ON c.CorrequisitoID = cr.id;



SELECT * FROM dbo.Asignacion
SELECT * FROM dbo.Curso
SELECT * FROM dbo.DiasSemana
SELECT * FROM dbo.EstadoCurso
SELECT * FROM dbo.Estudiante
SELECT * FROM dbo.Horario
SELECT * FROM dbo.Inscripcion
SELECT * FROM dbo.Prerrequisito
SELECT * FROM dbo.Correquisito
SELECT * FROM dbo.Materia
SELECT * FROM dbo.Profesor
SELECT * FROM dbo.Sala

SELECT 
    fk.name AS FK_name, 
    tp.name AS Parent_table, 
    tr.name AS Referenced_table
FROM sys.foreign_keys AS fk
INNER JOIN sys.tables AS tp ON fk.parent_object_id = tp.object_id
INNER JOIN sys.tables AS tr ON fk.referenced_object_id = tr.object_id
WHERE tr.name = 'Materia';

-- Eliminar las restricciones de claves foráneas que hacen referencia a Materia
ALTER TABLE Materia DROP CONSTRAINT FK__Materia__corequi__18D6A699;
ALTER TABLE Prerrequisito DROP CONSTRAINT FK__Prerrequi__Mater__2818EA29;
ALTER TABLE Prerrequisito DROP CONSTRAINT FK__Prerrequi__Prerr__290D0E62;
*/
