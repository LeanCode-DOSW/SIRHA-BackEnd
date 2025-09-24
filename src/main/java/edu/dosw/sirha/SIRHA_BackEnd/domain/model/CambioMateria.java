package edu.dosw.sirha.SIRHA_BackEnd.domain.model;

public class CambioMateria extends BaseRequest {
    private String fromCourseId;
    private String toCourseId;

    public CambioMateria(int prioridad, String fromCourseId, String toCourseId) {
        super(prioridad);
        this.fromCourseId = fromCourseId;
        this.toCourseId = toCourseId;
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
