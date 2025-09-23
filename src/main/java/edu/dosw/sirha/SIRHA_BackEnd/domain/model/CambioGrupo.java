package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class CambioGrupo extends BaseRequest {
    private String fromGroupId;   // cada id es unica entre todas las materias
    private String toGroupId;

    public CambioGrupo(int prioridad, String fromGroupId, String toGroupId) {
        super(prioridad);
        this.fromGroupId = fromGroupId;
        this.toGroupId = toGroupId;
    }

    @Override
    public boolean validar() {
        return false;
    }

    @Override
    public boolean aplicar() {
        return false;
    }

    @Override
    public void aprobar() {
    }

    @Override
    public void rechazar() {
    }

}
