package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Messages.CustomMessage;
import com.example.lazyco.core.Messages.MessageCodes;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardMessageDTO {

  public enum MessageType {
    INFO,
    WARNING,
    ERROR,
    EXCEPTION
  }

  /* =========================
  EXTERNAL STORAGE (ONLY)
  ========================= */

  private Set<MessageInnerObject> error;
  private Set<MessageInnerObject> warning;
  private Set<MessageInnerObject> info;
  private Set<MessageInnerObject> exception;

  public Set<MessageInnerObject> getError() {
    if (error == null) error = new HashSet<>();
    return error;
  }

  public Set<MessageInnerObject> getWarning() {
    if (warning == null) warning = new HashSet<>();
    return warning;
  }

  public Set<MessageInnerObject> getInfo() {
    if (info == null) info = new HashSet<>();
    return info;
  }

  public Set<MessageInnerObject> getException() {
    if (exception == null) exception = new HashSet<>();
    return exception;
  }

  /* =========================
  INTERNAL STORAGE (ONLY)
  ========================= */
  private transient Map<String, Set<String>> infoMap;
  private transient Map<String, Set<String>> warningMap;
  private transient Map<String, Set<String>> errorMap;
  private transient Map<String, Set<String>> exceptionMap;

  private Map<String, Set<String>> getInfoMap() {
    if (infoMap == null) infoMap = new HashMap<>();
    return infoMap;
  }

  private Map<String, Set<String>> getWarningMap() {
    if (warningMap == null) warningMap = new HashMap<>();
    return warningMap;
  }

  private Map<String, Set<String>> getErrorMap() {
    if (errorMap == null) errorMap = new HashMap<>();
    return errorMap;
  }

  private Map<String, Set<String>> getExceptionMap() {
    if (exceptionMap == null) exceptionMap = new HashMap<>();
    return exceptionMap;
  }

  /* =========================
  PUBLIC READ API (SAFE)
  ========================= */

  public Map<String, Set<String>> getInfoMessages() {
    return unmodifiableCopy(getInfoMap());
  }

  public Map<String, Set<String>> getWarningMessages() {
    return unmodifiableCopy(getWarningMap());
  }

  public Map<String, Set<String>> getErrorMessages() {
    return unmodifiableCopy(getErrorMap());
  }

  public Map<String, Set<String>> getExceptionMessages() {
    return unmodifiableCopy(getExceptionMap());
  }

  private Map<String, Set<String>> unmodifiableCopy(Map<String, Set<String>> source) {
    Map<String, Set<String>> copy = new HashMap<>();
    source.forEach((k, v) -> copy.put(k, Set.copyOf(v)));
    return Collections.unmodifiableMap(copy);
  }

  /* =========================
  STATUS HELPERS
  ========================= */

  public boolean hasErrors() {
    return !getErrorMap().isEmpty() || !getExceptionMap().isEmpty();
  }

  public boolean hasWarnings() {
    return !getWarningMap().isEmpty();
  }

  public boolean hasInfo() {
    return !getInfoMap().isEmpty();
  }

  /* =========================
  WRITE API
  ========================= */

  public void addMessage(MessageType type, String message) {
    this.addMessage(type, null, message);
  }

  public void addMessage(MessageType type, MessageCodes message) {
    this.addMessage(type, null, CustomMessage.getMessageString(message));
  }

  public void addMessage(MessageType type, String code, String message) {
    if (message == null || message.isBlank()) return;

    if (code == null || code.isBlank()) {
      code = "GENERAL";
    }

    Map<String, Set<String>> map = getMapForType(type);
    Set<MessageInnerObject> set = getSetForType(type);
    if (map.containsKey(code)) {
      map.get(code).add(message);
    } else {
      Set<String> messageSet = new HashSet<>();
      messageSet.add(message);
      map.put(code, messageSet);
      MessageInnerObject messageInnerObject = new MessageInnerObject(code, messageSet);
      set.add(messageInnerObject);
    }
  }

  private Map<String, Set<String>> getMapForType(MessageType type) {
    return switch (type) {
      case INFO -> getInfoMap();
      case WARNING -> getWarningMap();
      case ERROR -> getErrorMap();
      case EXCEPTION -> getExceptionMap();
    };
  }

  private Set<MessageInnerObject> getSetForType(MessageType type) {
    return switch (type) {
      case ERROR -> getError();
      case WARNING -> getWarning();
      case INFO -> getInfo();
      case EXCEPTION -> getException();
      default -> new HashSet<>();
    };
  }

  /* =========================
  MERGING
  ========================= */

  public static void mergeMessage(AbstractDTO<?> source, AbstractDTO<?> target) {
    if (source == null || source.getMessages() == null) return;

    if (target.getMessages() == null) {
      target.setMessages(new StandardMessageDTO());
    }

    target.getMessages().merge(source.getMessages());
  }

  public void merge(StandardMessageDTO other) {
    if (other == null) return;

    mergeMap(this.getInfoMap(), other.getInfoMap());
    mergeMap(this.getWarningMap(), other.getWarningMap());
    mergeMap(this.getErrorMap(), other.getErrorMap());
    mergeMap(this.getExceptionMap(), other.getExceptionMap());
  }

  private void mergeMap(Map<String, Set<String>> target, Map<String, Set<String>> source) {
    source.forEach(
        (code, messages) -> target.computeIfAbsent(code, k -> new HashSet<>()).addAll(messages));
  }

  /* =========================
  UTILITY
  ========================= */

  public Set<String> getAllMessages() {
    Set<String> result = new HashSet<>();
    collectMessages(getInfoMap(), result);
    collectMessages(getWarningMap(), result);
    collectMessages(getErrorMap(), result);
    collectMessages(getExceptionMap(), result);
    return result;
  }

  private void collectMessages(Map<String, Set<String>> source, Set<String> target) {
    source.values().forEach(target::addAll);
  }

  @Getter
  @Setter
  private static class MessageInnerObject {
    private String code;
    private Set<String> messages;

    public MessageInnerObject(String code, Set<String> messages) {
      this.code = code;
      this.messages = messages;
    }
  }
}
