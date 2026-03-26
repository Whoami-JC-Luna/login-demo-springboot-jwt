ALTER TABLE quotes ADD COLUMN user_id UUID REFERENCES users(id) ON DELETE CASCADE;

INSERT INTO quotes (texto, autor, user_id) VALUES
('El secreto para avanzar es comenzar.', 'Mark Twain', NULL),
('Caminante, no hay camino, se hace camino al andar.', 'Antonio Machado', NULL),
('No importa lo lento que vayas, siempre que no te detengas.', 'Confucio', NULL),
('El trabajo y la constancia son la base de todo progreso.', 'Benito Pérez Galdós', NULL),
('La perseverancia es fallar 19 veces y acertar la 20.', 'Julie Andrews', NULL),
('Cada día es una nueva oportunidad.', 'Jorge Luis Borges', NULL),
('La vida es lo que pasa mientras estás ocupado haciendo otros planes.', 'John Lennon', NULL),
('En medio de toda dificultad se encuentra la oportunidad.', 'Albert Einstein', NULL),
('Lo que no te mata te hace más fuerte.', 'Friedrich Nietzsche', NULL),
('La única forma de hacer un gran trabajo es amar lo que haces.', 'Steve Jobs', NULL),
('Primero lo necesario, luego lo posible, y de repente estarás haciendo lo imposible.', 'Francisco de Asís', NULL),
('No importa lo despacio que vayas, siempre que no te detengas.', 'Confucio', NULL),
('El éxito es la suma de pequeños esfuerzos repetidos día tras día.', 'Robert Collier', NULL),
('Cree que puedes y ya estarás a mitad de camino.', 'Theodore Roosevelt', NULL),
('La imaginación es más importante que el conocimiento.', 'Albert Einstein', NULL),
('Fallas el 100% de los tiros que no intentas.', 'Wayne Gretzky', NULL),
('El único modo de hacer un gran trabajo es amar lo que haces.', 'Steve Jobs', NULL),
('El éxito no es definitivo, el fracaso no es fatal: lo que cuenta es el valor para continuar.', 'Winston Churchill', NULL)
