
#!/bin/bash

echo "======================================"
echo "  Iniciando Red de Negocios"
echo "======================================"
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 1. Verificar que SQL Server esté corriendo
echo -e "${YELLOW}[1/3] Verificando SQL Server...${NC}"
if sudo docker ps | grep -q sqlserver; then
    echo -e "${GREEN}✓ SQL Server está corriendo${NC}"
else
    echo -e "${RED}✗ SQL Server no está corriendo. Iniciando...${NC}"
    sudo docker start sqlserver
    sleep 5
    echo -e "${GREEN}✓ SQL Server iniciado${NC}"
fi
echo ""

# 2. Iniciar Backend (Spring Boot)
echo -e "${YELLOW}[2/3] Iniciando Backend (Spring Boot en puerto 8080)...${NC}"
cd "/home/edreo/CoNetlng /domain-layer"
echo -e "${GREEN}Backend iniciando... (Espera a ver 'Started NegociosApplication')${NC}"
./mvnw spring-boot:run &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"
echo ""

# Esperar a que el backend inicie
echo "Esperando a que el backend esté listo..."
sleep 15

# 3. Iniciar Frontend (React)
echo -e "${YELLOW}[3/3] Iniciando Frontend (React en puerto 3000)...${NC}"
cd "/home/edreo/CoNetlng /interface-layer"
npm start &
FRONTEND_PID=$!
echo "Frontend PID: $FRONTEND_PID"
echo ""

echo "======================================"
echo -e "${GREEN}✓ Todo está en ejecución!${NC}"
echo "======================================"
echo ""
echo "Accede a la aplicación en:"
echo -e "${GREEN}http://localhost:3000${NC}"
echo ""
echo "Para detener todo, presiona Ctrl+C"
echo ""

# Esperar a que el usuario termine
wait
