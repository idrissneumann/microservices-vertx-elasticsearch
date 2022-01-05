--arrêt des connexions de la base de donnée --
--unix
SELECT pg_terminate_backend(procpid) FROM pg_stat_activity WHERE datname = 'bblvertx';
--Windows
--SELECT pg_terminate_backend (pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'bblvertx';
DROP DATABASE bblvertx;