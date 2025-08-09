FROM postgres:15

# Set environment variables
ENV POSTGRES_DB=leave_management
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Copy initialization script
COPY ../../database/init.sql /docker-entrypoint-initdb.d/

# Expose port
EXPOSE 5432
